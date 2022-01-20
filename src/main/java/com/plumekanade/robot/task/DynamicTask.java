package com.plumekanade.robot.task;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.plumekanade.robot.constants.APIConst;
import com.plumekanade.robot.constants.PixivConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.constants.SysKeyConst;
import com.plumekanade.robot.entity.BotTask;
import com.plumekanade.robot.entity.CookieLib;
import com.plumekanade.robot.handler.BotEventHandler;
import com.plumekanade.robot.service.*;
import com.plumekanade.robot.utils.CommonUtils;
import com.plumekanade.robot.utils.MiHoYoUtils;
import com.plumekanade.robot.utils.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.message.data.PlainText;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import javax.annotation.Resource;
import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


/**
 * 可动态调整的定时任务
 *
 * @author kanade
 * @version 1.0
 * @date 2021-02-02 09:25:45
 */
@Slf4j
@Configuration
public class DynamicTask implements SchedulingConfigurer {

  // 11:00、21:00 发送提醒
  public static String REMIND_CRON = "0 0 12 * * ?";
  public static String SIGN_CRON = "0 0 7 * * ?";
  // 00:10
  public static String WEI_BO_SIGN_CRON = "0 10 0 * * ?";
  public static String GREET_CRON = "0 0 9,15,18,21 * * ?";
  // 图片是否已加入到msgBuilder
  public static boolean IMG_ADDED = false;

  @Resource(name = "bot")
  private Bot bot;
//  @Resource(name = "kanadeBot")
//  private Bot kanadeBot;
  @Resource
  private GalleryService galleryService;
  @Resource
  private BotTaskService botTaskService;
  @Resource
  private CookieLibService cookieLibService;
  @Resource
  private SystemConfigService systemConfigService;
  @Resource
  private BotFunctionWordService botFunctionWordService;


  @Override
  public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

    // 定时签到
    taskRegistrar.addTriggerTask(this::signTask, context -> new CronTrigger(SIGN_CRON).nextExecutionTime(context));

    // 定时提醒
    taskRegistrar.addTriggerTask(this::remindTask, context -> new CronTrigger(REMIND_CRON).nextExecutionTime(context));

//    // 定时打招呼
//    taskRegistrar.addTriggerTask(this::greetTask, context -> new CronTrigger(GREET_CRON).nextExecutionTime(context));

    // 微博自动签到
    taskRegistrar.addTriggerTask(this::weiBoSignTask, context -> new CronTrigger(WEI_BO_SIGN_CRON).nextExecutionTime(context));
  }

  /**
   * 定时签到任务
   */
  public void signTask() {
    log.info("===============================每天签到任务启动===============================");
    long sleep = 60000L;
    List<CookieLib> list = cookieLibService.list(new LambdaQueryWrapper<CookieLib>().eq(CookieLib::getState, 1));
    for (CookieLib cookieLib : list) {

      try {   // 随机延迟 60秒~120秒
        Thread.sleep(CommonUtils.RANDOM.nextLong(sleep) + sleep);
      } catch (Exception e) {
        log.error("【延时】延时签到出现异常, 堆栈信息: ", e);
      }

      try {
        // 签到成功不发送消息
//          BotListener.COMMON_SENDER.SENDER.sendGroupMsg(ProjectConst.LI_YUE, MiHoYoUtils.sign(relate.getCookie()));
        log.info("【原神签到】签到结果: " + MiHoYoUtils.sign(cookieLib.getMhyCookie()));
      } catch (Exception e) {
        log.error("【原神签到】签到异常, 异常堆栈: ", e);
        // 向管理员发送消息
        Friend master = bot.getFriend(Long.parseLong(ProjectConst.CONFIG_MAP.get(SysKeyConst.QQ)));
        if (null != master) {
          master.sendMessage("QQ: " + cookieLib.getQq() + "\n游戏UID: " + cookieLib.getYsId() + "\n签到异常");
        }
      }
    }
    log.info("===============================每天签到任务结束===============================");
  }

  /**
   * 定时提醒任务
   */
  public void remindTask() {
    log.info("===============================定时提醒任务启动===============================");
    IMG_ADDED = false;
    MessageChainBuilder msgBuilder = new MessageChainBuilder();
    int[] time = CommonUtils.getSpiralAbyssSurplusDays();
    msgBuilder.append(new PlainText(handleWeiboMsg() + "米游社该签到了。\n距离深渊刷新还有" + time[0] + "天" + time[1] + "小时\n" + handleExtraMsg()));
    File img = null;
    if (Boolean.parseBoolean(ProjectConst.CONFIG_MAP.get(SysKeyConst.REMIND_IMG))) {
      img = new File(galleryService.randomImg(Integer.parseInt(ProjectConst.CONFIG_MAP.get(SysKeyConst.REMIND_IMG_SEXY)), null));
    }
    log.info("【定时提醒】消息内容: " + msgBuilder);

    String groupIds = ProjectConst.CONFIG_MAP.get(SysKeyConst.REMIND_GROUP);
    for (String groupId : groupIds.split(Constants.COMMA)) {
      Group group = bot.getGroup(Long.parseLong(groupId));
      handleRemind(group, groupId, img, msgBuilder);
//      group = bot.getGroup(Long.parseLong(groupId));
//      handleRemind(group, groupId, img, msgBuilder);
    }
    log.info("===============================定时提醒任务结束===============================");
  }

  private void handleRemind(Group group, String groupId, File img, MessageChainBuilder msgBuilder) {
    if (null == group) {
      log.info("【定时提醒】已被移出群聊 {}, 提醒任务结束!", groupId);
      return;
    }
    try {
      if (!IMG_ADDED && null != img && img.exists()) {
        msgBuilder.append(Contact.uploadImage(group, img));
        IMG_ADDED = true;
      }
      group.sendMessage(msgBuilder.build());
    } catch (Exception e) {
      log.error("【定时提醒】任务异常, 堆栈信息: ", e);
    }
  }

  /**
   * 打招呼
   */
  public void greetTask() {
    int type = 1;
    long groupId = Long.parseLong(ProjectConst.CONFIG_MAP.get(SysKeyConst.MU_JIAN));
    Group group = bot.getGroup(groupId);
    if (null == group) {
      log.error("【打招呼】机器人不存在群 {}, 打招呼任务结束!", groupId);
      return;
    }

    switch (LocalDateTime.now().getHour()) {
//      case 12 -> type = 2;
      case 15 -> type = 3;
      case 18 -> type = 4;
      case 21 -> type = 5;
    }
    List<String> words = botFunctionWordService.getWords(type);
    MessageChainBuilder msgBuilder = new MessageChainBuilder();
    BotEventHandler.checkImgMsg(words.get(CommonUtils.RANDOM.nextInt(words.size())), group, msgBuilder);
    group.sendMessage(msgBuilder.build());
  }

  /**
   * 微博自动签到
   */
  public void weiBoSignTask() {
    log.info("\n===============================微博自动签到===============================");
    List<CookieLib> list = cookieLibService.list(new LambdaQueryWrapper<CookieLib>()
        .isNotNull(CookieLib::getWeiboCookie).eq(CookieLib::getState, ProjectConst.ONE));
    try {
      for (CookieLib cookieLib : list) {
        if (StringUtils.isNotBlank(cookieLib.getWeiboCookie())) {
          Header[] headers = new Header[4];
          headers[0] = new BasicHeader("cookie", cookieLib.getWeiboCookie());
          headers[1] = new BasicHeader(PixivConst.REFERER_KEY, APIConst.WEI_BO_REFERER);
          headers[2] = new BasicHeader(APIConst.CONTENT_TYPE, APIConst.CONTENT_TYPE_X_WWW);
          headers[3] = new BasicHeader("x-requested-with", "XMLHttpRequest");
          log.info("【微博签到】签到结果: " + ServletUtils.get(APIConst.WEI_BO_SIGN, headers));
        }
      }
    } catch (Exception e) {
      log.error("【微博签到】签到异常, 堆栈信息: ", e);
    }
    log.info("\n===============================微博自动签到===============================");
  }

  /**
   * 遍历处理额外消息
   */
  public String handleExtraMsg() {
    List<BotTask> activeList = botTaskService.getActiveList(null);
    StringBuilder extraMsg = new StringBuilder();
    LocalDateTime now = LocalDateTime.now();

    for (BotTask botTask : activeList) {
      long hours = Duration.between(now, botTask.getExpireTime()).toHours();    // 相差的小时数
      long days = hours / 24;
      hours = hours % 24;
      if (days > 0) {
        extraMsg.append(botTask.getMsg().replace(ProjectConst.REPLACE_CHAR, days + "天" + hours + "小时"));
      } else {    // 即将结束
        extraMsg.append(botTask.getMsg().replace(ProjectConst.REPLACE_CHAR, hours + "小时"));
        botTask.setState(ProjectConst.ZERO);
        botTaskService.updateById(botTask);
      }
      extraMsg.append(ProjectConst.WRAP);
    }
    return extraMsg.toString();
  }

  /**
   * 处理微博是否需要提醒
   */
  public String handleWeiboMsg() {
    int num = Integer.parseInt(ProjectConst.CONFIG_MAP.get(SysKeyConst.WEIBO_NUM));
    if (num > 0) {
      String surplusNum = String.valueOf(--num);
      systemConfigService.setVal(SysKeyConst.WEIBO_NUM, surplusNum);
      ProjectConst.CONFIG_MAP.put(SysKeyConst.WEIBO_NUM, surplusNum);
      return "微博和";
    }
    return "";
  }

}
