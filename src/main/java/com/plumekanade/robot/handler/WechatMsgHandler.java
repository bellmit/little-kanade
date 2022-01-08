package com.plumekanade.robot.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.plumekanade.robot.constants.DateConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.constants.SysKeyConst;
import com.plumekanade.robot.entity.*;
import com.plumekanade.robot.service.*;
import com.plumekanade.robot.task.DynamicTask;
import com.plumekanade.robot.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.plumekanade.robot.constants.CmdConst.*;

/**
 * 默认消息事件
 *
 * @author kanade
 * @date 2021-10-28 11:35
 */
@Slf4j
@Component
@AllArgsConstructor
public class WechatMsgHandler implements WxMpMessageHandler {

  private final AcgTagService acgTagService;
  private final BotTaskService botTaskService;
  private final AcgRecordService acgRecordService;
  private final MemorandumService memorandumService;
  private final AccountDataService accountDataService;
  private final SystemConfigService systemConfigService;


  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService,
                                  WxSessionManager wxSessionManager) throws WxErrorException {
    // 普通消息事件处理 组装回复消息
    String content = wxMessage.getContent();
    // 敏感数据获取 0指令 1目标 / 0指令 1目标 2密码 / 0指令 1目标 2账号 3密码 4加密次数
    // 追番记录 0指令 1哪个季度(202107)
    // acg记录 0指令 1名称(模糊) / 0指令 1名称(模糊) 2进度 / 0指令 1名称(全称) 2进度 3状态(0在追1看完2弃) 4所属季度(202107) 5标签(多个逗号隔开)
    // 每日提醒语句添加额外消息 0指令 1标题(查询用) / 0指令 1标题 2消息内容 3过期时间(yyyy-MM-dd_HH:mm:ss)
    // 开启微博签到提醒 0指令 1连续提醒的天数
    // 每日提醒附带图片 0指令 1true/false 2图片等级0/1/2
    String[] msgArr = content.split(SEPARATOR2);
    String targetName = msgArr.length >= 2 ? msgArr[1] : null;
    try {
      switch (msgArr[0]) {
        // 指令查询
        case CMD_LIST -> content = """
            例:\s
            微博提醒#连续提醒的天数
            追番#202110
            追番列表
            ACG状态#无职转生#0(在看)1(已看完)2(弃看)
            弃番列表
            动画列表
            签到附图#true#1(0/1/2图片等级)
            签到附图#false#1
            任务列表#消息标题
            提醒任务#消息标题#消息内容#开始时间(2021-11-11 16:38:30)#截止时间(yyyy-MM-dd HH:mm:ss)
            账号(查询)#epic
            账号(修改)#epic#pwd
            账号(添加)#epic#username#pwd#count
            动画(漫画/小说)#无职
            动画(漫画/小说)#无职#4
            动画(漫画/小说)#无职转生#1#0(0在追1看完2弃)#202107(所属季度)#异世界,转生,魔法
            备忘录
            备忘录#标题
            备忘录#标题#内容
            备忘录#标题#内容#提醒时间
            """;
        // 修改番剧状态
        case ACG_STATE -> {
          content = "更改番剧/漫画/小说状态成功";
          if (msgArr.length != 3 || Integer.parseInt(msgArr[2]) > 2) {
            content = "请注意格式: 追番状态#无职转生 第二季#0(在看)1(已看完)2(弃看)";
            break;
          }
          acgRecordService.updateStateWithName(msgArr[1], msgArr[2]);
        }
        // 追番列表
        case ANIME_LIST -> content = handleAnimeList(acgRecordService.getAllAnimations(null, 0));
        // 弃番列表
        case ABANDON_ANIME_LIST -> content = handleAnimeList(acgRecordService.getAllAnimations(null, 2));
        // 所有动画
        case ALL_ANIME_LIST -> content = handleAnimeList(acgRecordService.getAllAnimations(null, null));
        // 查询某季度的动画
        case ANIME_QUERY -> content = handleAnimeList(acgRecordService.getAllAnimations(targetName, null));
        case ACCOUNT -> {
          if (msgArr.length <= 2) {   // 查询账号
            content = getAccount(targetName);
          } else if (msgArr.length == 3) {   // 改密码
            content = accountDataService.wxModifyPwd(targetName, msgArr[2]);
            if (null == content) {
              content = "已成功修改目标 " + targetName;
            }
          } else {
            content = accountDataService.addData(msgArr);
            if (null == content) {
              content = "已成功添加目标 " + targetName;
            }
          }
        }
        case MEMORANDUM -> {
          content = "已成功添加备忘录\t - \t" + targetName;
          LambdaQueryWrapper<Memorandum> wrapper = new LambdaQueryWrapper<>();
          if (null == targetName) {   // 查询所有等待提醒或不需要提醒的备忘录
            wrapper.eq(Memorandum::isRemindFlg, false);
            wrapper.or(w -> w.eq(Memorandum::isRemindFlg, true).eq(Memorandum::getState, 1));
            content = handleMemorandum(memorandumService.list(wrapper));
          } else if (2 == msgArr.length) {    // 查询指定标题(模糊)备忘录
            wrapper.like(Memorandum::getTitle, targetName);
            content = handleMemorandum(memorandumService.list(wrapper));
          } else if (3 == msgArr.length) {    // 添加/更新不需要提醒的备忘录
            memorandumService.saveOrUpdateByTitle(targetName, msgArr[2], null);
          } else {    // 添加/更新需要提醒的备忘录
            memorandumService.saveOrUpdateByTitle(targetName, msgArr[2], DateConst.SDF.parse(msgArr[3]));
          }
        }
        case ANIME -> {
          List<AcgRecord> animations = acgRecordService.getAnimations(targetName);
          if (msgArr.length == 3) {    // 更新进度
            content = handleUpdateProgress(animations, msgArr[2]);
          } else if (msgArr.length == 2) {
            content = handleQueryAcg(animations);
          } else {    // 添加新动画
            if (verifyDuplicate(targetName, animations)) {
              handleTag(msgArr[3]);
              content = "添加动画 " + targetName + " 成功";
              acgRecordService.save(new AcgRecord(targetName, msgArr[2], msgArr[5], 0, Integer.parseInt(msgArr[3]), msgArr[4]));
            } else {
              content = "已有相同名称 " + targetName + " 的动画存在";
            }
          }
        }
        case COMIC -> {
          List<AcgRecord> comics = acgRecordService.getComics(targetName);
          if (msgArr.length == 3) {   // 更新漫画进度
            content = handleUpdateProgress(comics, msgArr[2]);
          } else if (msgArr.length == 2) {  // 查询漫画进度
            content = handleQueryAcg(comics);
          } else {    // 添加新漫画
            if (verifyDuplicate(targetName, comics)) {
              handleTag(msgArr[3]);
              content = "添加漫画 " + targetName + " 成功";
              acgRecordService.save(new AcgRecord(targetName, msgArr[2], msgArr[5], 1, Integer.parseInt(msgArr[3]), msgArr[4]));
            } else {
              content = "已有相同名称 " + targetName + " 的漫画存在";
            }
          }
        }
        case NOVEL -> {
          List<AcgRecord> novels = acgRecordService.getNovels(msgArr[1]);
          if (msgArr.length == 3) {    // 更新进度
            content = handleUpdateProgress(novels, msgArr[2]);
          } else if (msgArr.length == 2) {
            content = handleQueryAcg(novels);
          } else {    // 添加新小说
            if (verifyDuplicate(targetName, novels)) {
              handleTag(msgArr[3]);
              content = "添加小说 " + targetName + " 成功";
              acgRecordService.save(new AcgRecord(targetName, msgArr[2], msgArr[5], 2, Integer.parseInt(msgArr[3]), msgArr[4]));
            } else {
              content = "已有相同名称 " + targetName + " 的小说存在";
            }
          }
        }
        case REMIND_TASK -> {
          BotTask botTask = botTaskService.getActiveTask(targetName);
          LocalDateTime expireTime = LocalDateTime.parse(msgArr[4], DateConst.DTF);
          if (null != botTask) {  // 更新原来的任务
            botTask.setMsg(msgArr[2]);
            botTask.setStartTime(DateConst.SDF.parse(msgArr[3]));
            botTask.setExpireTime(expireTime);
            botTaskService.updateById(botTask);
          } else {
            botTask = new BotTask(targetName, msgArr[2], DateConst.SDF.parse(msgArr[3]), expireTime, ProjectConst.ONE);
            botTaskService.save(botTask);
          }
          long hours = Duration.between(LocalDateTime.now(), expireTime).toHours();    // 相差的小时数
          long days = hours / 24;
          hours = hours % 24;
          String text = days > 0 ? days + "天" + hours + "小时" : hours + "小时";
          String demo = botTask.getMsg().replace(ProjectConst.REPLACE_CHAR, text);
          content = "已设置签到提醒额外消息: " + demo;
        }
        case TASK_LIST -> {
          List<BotTask> activeList = botTaskService.getActiveList(targetName);
          StringBuilder builder = new StringBuilder();
          for (BotTask botTask : activeList) {
            builder.append("标题: ").append(botTask.getName()).append(ProjectConst.WRAP)
                .append("内容: ").append(botTask.getMsg()).append(ProjectConst.WRAP)
                .append("开始时间: ").append(DateConst.SDF.format(botTask.getStartTime())).append(ProjectConst.WRAP)
                .append("结束时间: ").append(DateConst.DTF.format(botTask.getExpireTime())).append("\n\n");
          }
          content = builder.length() > 0 ? builder.substring(0, builder.length() - 2) : "没有执行任何任务...";
        }
        case WEIBO_REMIND -> {
          systemConfigService.setVal(SysKeyConst.WEIBO_NUM, StringUtils.isNotBlank(targetName) ? targetName : ProjectConst.ZERO);
          content = "已设置微博签到连续提醒" + targetName + "天";
        }
        case ATTACH_IMG -> {
          systemConfigService.setVal(SysKeyConst.REMIND_IMG, targetName);
          systemConfigService.setVal(SysKeyConst.REMIND_IMG_SEXY, msgArr[2]);
          content = "已设置" + (Boolean.parseBoolean(targetName) ? "附带" : "不附带") + "图片";
        }
      }
    } catch (Exception e) {
      log.error("【微信消息】组装指令回复出现异常, 堆栈信息: ", e);
      content = "服务器异常, 请稍后再试";
    }
    return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
  }

  /**
   * 账号相关敏感数据指令
   */
  public String getAccount(String target) {
    LambdaQueryWrapper<AccountData> wrapper = new LambdaQueryWrapper<>();
    if (StringUtils.isNotBlank(target)) {
      wrapper.like(AccountData::getTarget, target);
    }
    List<AccountData> list = accountDataService.list(wrapper);
    StringBuilder builder = new StringBuilder();
    for (AccountData accountData : list) {
      String raw = CommonUtils.decrypt(accountData.getCipherText(), accountData.getCount());
      String[] split = raw.split(CommonUtils.SECRET);
      builder.append(ProjectConst.DIVIDER).append(ProjectConst.WRAP)
          .append(accountData.getTarget()).append(ProjectConst.WRAP)
          .append(split[0]).append(ProjectConst.WRAP)
          .append(split[1]).append(ProjectConst.WRAP)
          .append(ProjectConst.DIVIDER).append(ProjectConst.WRAP);
    }
    return builder.length() > 0 ? builder.substring(0, builder.length() - 1) : "未查询到任何匹配的数据";
  }

  /**
   * 更新acg进度操作提取
   */
  public String handleUpdateProgress(List<AcgRecord> records, String progress) {
    if (records.size() > 1) {    // 返回所有匹配条目
      StringBuilder builder = new StringBuilder();
      builder.append("匹配到多条符合条件的数据, 请详写名称: \n");
      records.forEach(a -> builder.append(a.getAcgName()).append(ProjectConst.WRAP));
      return builder.substring(0, builder.length() - 1);
    } else {
      AcgRecord acgRecord = records.get(0);
      acgRecord.setProgress(progress);
      acgRecordService.updateById(acgRecord);
      String typeName = acgRecord.getType() == 0 ? "动画 " : (acgRecord.getType() == 1 ? "漫画 " : "小说 ");
      return typeName + acgRecord.getAcgName() + " 进度更新为: " + acgRecord.getProgress();
    }
  }

  /**
   * 校验是否未重复
   */
  public boolean verifyDuplicate(String targetName, List<AcgRecord> recordList) {
    for (AcgRecord animation : recordList) {
      if (targetName.equalsIgnoreCase(animation.getAcgName())) {
        return false;
      }
    }
    return true;
  }

  /**
   * 标签处理
   */
  public void handleTag(String tag) {
    String[] tags = tag.split(Constants.COMMA);
    List<AcgTag> list = acgTagService.list(new LambdaQueryWrapper<AcgTag>().in(AcgTag::getName, Arrays.asList(tags)));
    if (list.size() > 0 && list.size() < tags.length) {
      for (String tagName : tags) {
        boolean flg = true;
        for (AcgTag acgTag : list) {
          if (tagName.equals(acgTag.getName())) {   // 已存在tag 跳过
            flg = false;
            list.remove(acgTag);
            break;
          }
        }
        if (flg) {  // 新tag 入库
          acgTagService.save(new AcgTag(tagName));
        }
      }
    }
  }

  /**
   * 进度查询
   */
  public String handleQueryAcg(List<AcgRecord> records) {
    String content = "没有找到匹配的动画/漫画/小说";
    if (records.size() > 0) {
      StringBuilder builder = new StringBuilder();
      for (AcgRecord record : records) {
        builder.append(record.getAcgName()).append("\t - \t").append(record.getProgress()).append(ProjectConst.WRAP);
      }
      content = builder.substring(0, builder.length() - 1);
    }
    return content;
  }

  /**
   * 动画列表格式处理
   */
  public String handleAnimeList(List<AcgRecord> list) {
    StringBuilder builder = new StringBuilder();
    list.forEach(item -> builder.append(item.getAcgName()).append("\t - \t").append(item.getProgress()).append(ProjectConst.WRAP));
    return builder.length() > 0 ? builder.substring(0, builder.length() - 1) : "无";
  }

  /**
   * 组装备忘录
   */
  public String handleMemorandum(List<Memorandum> list) {
    StringBuilder builder = new StringBuilder();
    list.forEach(item -> {
      builder.append(item.getTitle()).append("\t - \t").append(item.getContent());
      if (item.isRemindFlg()) {
        builder.append("\t - \t").append(item.getRemindTime());
      }
      builder.append(ProjectConst.WRAP);
    });
    if (builder.length() > 0) {
      return builder.substring(0, builder.length() - 1);
    }
    return "未找到相关备忘录数据";
  }

}
