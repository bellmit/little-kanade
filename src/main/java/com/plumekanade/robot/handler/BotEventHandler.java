package com.plumekanade.robot.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.plumekanade.robot.constants.BotConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.entity.CookieLib;
import com.plumekanade.robot.entity.GroupConfig;
import com.plumekanade.robot.entity.Tarot;
import com.plumekanade.robot.service.*;
import com.plumekanade.robot.utils.*;
import com.plumekanade.robot.vo.GenshinInfo;
import com.plumekanade.robot.vo.LoLiConReq;
import com.plumekanade.robot.vo.LoLiConResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.event.events.NudgeEvent;
import net.mamoe.mirai.internal.network.protocol.packet.chat.TroopManagement;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.*;

import static com.plumekanade.robot.constants.CmdConst.*;

/**
 * @author kanade
 * @date 2021-11-29 23:15
 */
@Slf4j
@Component
@AllArgsConstructor
public class BotEventHandler extends SimpleListenerHost {

  private final TarotService tarotService;
  private final BotDicService botDicService;
  private final BotChatService botChatService;
  private final RedisCertUtils redisCertUtils;
  private final RedisChatUtils redisChatUtils;
  private final GalleryService galleryService;
  private final CookieLibService cookieLibService;
  private final GroupConfigService groupConfigService;

  /**
   * 处理私聊消息
   */
  @EventHandler
  public void handlePrivateMsg(@NotNull FriendMessageEvent event) {
    Friend friend = event.getFriend();
    log.info("【私聊】收到 {} - {} 的消息: {}", friend.getId(), friend.getNick(), MessageChain.serializeToJsonString(event.getMessage()));

    String msgText = event.getMessage().contentToString();
    if (msgText.contains("cookie_token") && msgText.contains("account_id")) {
      String result;
      try {
        result = MiHoYoUtils.getRoles(msgText, false);
      } catch (Exception e) {
        log.error("【添加签到账号】获取角色失败, 堆栈信息: ", e);
        result = "获取角色异常，请确认是否绑定米哈游账号";
      }
      if (result.contains("uid")) {
        result = "添加/更新自动签到角色成功\n密码修改后请及时更新Cookie\n" + result;

        // 存入 qq->cookie 记录
        String uid = result.split("uid: ")[1];
        uid = uid.split("\n角色名称: ")[0];
        CookieLib cookieLib = cookieLibService.getByYsId(uid);
        if (cookieLib == null) {
          cookieLib = new CookieLib(friend.getId(), uid, msgText);
          cookieLibService.save(cookieLib);
        } else {
          cookieLib.setMhyCookie(msgText);
          cookieLibService.updateById(cookieLib);
        }
      }
      friend.sendMessage(result);
//      return;
    }

    // 取消私聊指令 太麻烦了
//    if (msgText.startsWith(SEPARATOR2)) {
//      handleCmd(null, friend, msgText.replace(SEPARATOR2, ""));
//      return;
//    }

  }

  /**
   * 处理群消息
   */
  @EventHandler
  private void handleGroupMsg(@NotNull GroupMessageEvent event) {

    String msg = event.getMessage().contentToString();
    log.info("【群消息】收到 {} - {}({}) 的消息: {}", event.getGroup().getName(), event.getSenderName(), event.getSender().getId(), msg);
    log.info("【群消息】" + event.getMessage().serializeToMiraiCode());

    if (msg.startsWith(SEPARATOR2)) {
      handleCmd(event, msg.replace(SEPARATOR2, ""));
      return;
    }

    // 记录文本, 复读
    if (BotConst.REPEAT_MODE) {
      String groupCode = String.valueOf(event.getGroup().getId());
      // 冷却中直接结束 双校验防并发
      if (redisChatUtils.isRepeatCooling(groupCode)) {
        return;
      }
      synchronized (msg.intern()) {
        String prevMsg = redisChatUtils.getRepeatRecord(groupCode);
        // 未处于冷却状态
        if (!redisChatUtils.isRepeatCooling(groupCode)) {
          if (msg.equals(prevMsg)) {
            event.getGroup().sendMessage(msg);   // 复读
            redisChatUtils.setRepeatCooling(groupCode);   // 设置冷却
            redisChatUtils.setRepeatRecord(groupCode, "");  // 重置上一条聊天记录
          } else {
            redisChatUtils.setRepeatRecord(groupCode, msg);   // 保存聊天记录, 留作下次复读比照
          }
        }
      }
    }
  }

  /**
   * 处理群戳一戳
   */
  @EventHandler
  private void handleGroupNudge(@NotNull NudgeEvent event, GroupMessageEvent groupEvent) {
    Long botId = event.getBot().getId();
    UserOrBot from = event.getFrom();
    UserOrBot target = event.getTarget();
    log.info("【戳一戳】{}({}) {} {}({})", from.getNick(), from.getId(), event.getAction(), target.getNick(), target.getId());
    if (!botId.equals(target.getId())) {
      return;
    }
    List<String> replyList = botChatService.getNudges();
    int size = replyList.size();
    int i = CommonUtils.RANDOM.nextInt(size + 1);
//    if (i == size) {
//
//    }
  }


  /**
   * 指令处理
   *
   * @date 2021-08-24 16:04
   */
  private void handleCmd(GroupMessageEvent groupMsgEvent, String msgText) {
    Member sender = groupMsgEvent.getSender();
    Group group = groupMsgEvent.getGroup();
    String[] msgArr = msgText.split(SEPARATOR);
    MessageChainBuilder msgBuilder = new MessageChainBuilder();

    switch (msgArr[0]) {
      // 指令列表
      case CMD_LIST -> msgBuilder.append("""
          #随机图片
          #随机涩图@jk|少女@女仆|猫耳
          #账号查询@101010101
          #查号@101010101
          #查号2(此指令为B服)@101010101
          #丘丘语(undone)@gusha
          #每日塔罗

          小功能：私聊直接发送米游社Cookie可执行米游社的原神自动签到功能
          """);
      case QUERY_ACCOUNT, ACCOUNT_QUERY -> msgBuilder.append(handleQA(msgArr, ProjectConst.OFFICIAL_SERVER));
      case QUERY_ACCOUNT2 -> msgBuilder.append(handleQA(msgArr, ProjectConst.B_SERVER));
      case GAIN_CHARACTER -> {
        try {
          CookieLib cookieLib = cookieLibService.getCookie(sender.getId());
          if (null != cookieLib) {
            msgBuilder.append(MiHoYoUtils.getRoles(cookieLib.getMhyCookie(), false));
          } else {
            msgBuilder.append("该Q号未绑定角色，无法查询");
          }
        } catch (Exception e) {
          log.error("【角色异常】获取米哈游角色异常, 堆栈信息: ", e);
          msgBuilder.append("获取角色异常，请确认是否绑定米哈游账号");
        }
      }
      case RANDOM_IMAGE -> {
        String groupId = String.valueOf(group.getId());
        synchronized (groupId.intern()) {
          String result = handleCheckCooling(groupId);
          if (null != result) {
            msgBuilder.append(result);
            break;
          }
          redisCertUtils.setRandomImgCoolTime(groupId);
        }
        msgBuilder.append(Contact.uploadImage(group, new File(galleryService.randomImg(groupConfigService.getGroupSexy(groupId)))));
      }
      case UPDATE_GALLERY -> {
        if (BotConst.QQ.equals(String.valueOf(sender.getId()))) {
          galleryService.updateGallery(ProjectConst.NORMAL_GALLERY_PATH, 0);
          galleryService.updateGallery(ProjectConst.SEXY_GALLERY_PATH, 1);
          galleryService.updateGallery(ProjectConst.BARE_GALLERY_PATH, 2);
          msgBuilder.append("图库已更新完成");
        }
      }
      case QIU_QIU_TRANSLATION -> msgBuilder.append("小奏还没有学会丘丘语翻译呢");
      case RANDOM_SEXY -> handleRandomSexy(groupMsgEvent, msgBuilder, msgArr);
      case DAILY_TAROT -> handleDailyTarot(groupMsgEvent, msgBuilder);
      default -> msgBuilder.append("?");
    }

    // 发送消息
    group.sendMessage(msgBuilder.build());
  }

  /**
   * 查号逻辑提取
   */
  private String handleQA(String[] msgArr, String channel) {
    if (msgArr.length < 2) {
      return "游戏uid呢 你吃了？";
    }
    String gameId = msgArr[1];
    if (gameId.length() < 9) {
      return "游戏id不正确，请检查是否为游戏右下角的id";
    } else {
      try {
        GenshinInfo genshinInfo = MiHoYoUtils.genShinUserInfo(gameId, channel);
        return ConvertUtils.formatInfoData(gameId, channel, genshinInfo);
      } catch (Exception e) {
        log.info("【账号查询】查询玩家信息失败, 堆栈信息: ", e);
        return "查询用户 " + gameId + " 账号信息失败，可能是该用户未注册/使用过米游社/米游社未开启信息";
      }
    }
  }

  /**
   * 随机图片冷却时间判断
   */
  private String handleCheckCooling(String code) {
    Long coolTime = redisCertUtils.isRandomImgCooling(code);
    if (null != coolTime) {
      return "图片功能冷却中！！还剩" + ((coolTime - new Date().getTime()) / 1000) + "秒";
    }
    return null;
  }

  /**
   * 随机涩图处理逻辑
   */
  private void handleRandomSexy(GroupMessageEvent groupMsgEvent, MessageChainBuilder builder, String[] msgArr) {
    String memberCode = String.valueOf(groupMsgEvent.getSender().getId());
    String code = String.valueOf(groupMsgEvent.getGroup().getId());
    if (!BotConst.QQ.equals(memberCode)) {
      String result = handleCheckCooling(code);
      if (null != result) {
        builder.append(result);
        return;
      }
    }

    synchronized (code.intern()) {
      if (!BotConst.QQ.equals(memberCode)) {
        String result = handleCheckCooling(code);
        if (null != result) {
          builder.append(result);
          return;
        }
      }
      boolean cdFlag = true;

      // 校验禁止词
      for (String s : msgArr) {
        if (ProjectConst.FORBID_WORD.contains(s)) {
          builder.append("不可以色色！不可以色色！");
          break;
        }
      }
      if (builder.size() > 5) {
        return;
      }

      boolean sexyLvFlag = msgArr.length > 2 && ("1".equals(msgArr[1]) || "0".equals(msgArr[1]));
      if (sexyLvFlag && BotConst.QQ.equals(memberCode)) {
        builder.append("不可以这样哦~");
        cdFlag = false;
      } else {
        List<String> params = new ArrayList<>(Arrays.asList(msgArr));
        params.remove(0);
        if (sexyLvFlag) {
          params.remove(1);
        }
        List<LoLiConResult> results = ServletUtils.handleLoLiConReq(MapperUtils.serialize(new LoLiConReq(sexyLvFlag ? 1 : 0, 1, params)));
        if (results.size() > 0) {
          LoLiConResult loLiConResult = results.get(0);
          try {
            String url = loLiConResult.getUrls().getRegular();
            if (StringUtils.isBlank(url)) {
              url = loLiConResult.getUrls().getOriginal();
            }
            InputStream is = Objects.requireNonNull(ServletUtils.get(url)).getContent();
            builder.append(Contact.uploadImage(groupMsgEvent.getGroup(), is)).append(ProjectConst.WRAP)
                .append("作者：").append(loLiConResult.getAuthor()).append(" - ").append(loLiConResult.getPid().toString());
          } catch (Exception e) {
            log.error("【随机涩图】获取涩图出现异常, 堆栈信息: ", e);
            builder.append("涩涩大失败！接口异常，请联系主人处理吧~~");
          }
        } else {
          builder.append("再怎么找也找不到的啦，换一张吧");
          cdFlag = false;
        }
      }
      if (cdFlag && !BotConst.QQ.equals(memberCode)) {
        redisCertUtils.setRandomImgCoolTime(code);
      }
    }
  }

  /**
   * 每日塔罗逻辑
   */
  private void handleDailyTarot(GroupMessageEvent groupMessageEvent, MessageChainBuilder builder) {
    String accountCode = String.valueOf(groupMessageEvent.getSender().getId());
    synchronized (accountCode.intern()) {
      Tarot tarot = redisChatUtils.getTarot(accountCode);
      if (null == tarot) {
        List<Tarot> list = tarotService.list();
        tarot = list.get(CommonUtils.RANDOM.nextInt(list.size()));
        redisChatUtils.setTarot(accountCode, MapperUtils.serialize(tarot));
      }
      builder.append(tarot.getReverse() ? "判定！逆位 - " : "判定！顺位 - ").append(tarot.getTarotName()).append("\n牌面释义：").append(tarot.getMeaning())
          .append(ProjectConst.WRAP).append(Contact.uploadImage(groupMessageEvent.getGroup(), new File(tarot.getPath())));
    }
  }

}
