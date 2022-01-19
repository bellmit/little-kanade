package com.plumekanade.robot.handler;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.plumekanade.robot.config.BotConfig;
import com.plumekanade.robot.constants.APIConst;
import com.plumekanade.robot.constants.BotConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.entity.CookieLib;
import com.plumekanade.robot.entity.Player;
import com.plumekanade.robot.entity.Tarot;
import com.plumekanade.robot.service.*;
import com.plumekanade.robot.utils.*;
import com.plumekanade.robot.vo.GenshinInfo;
import com.plumekanade.robot.vo.LoLiConReq;
import com.plumekanade.robot.vo.LoLiConResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.*;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.code.MiraiCode;
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
@SuppressWarnings({"ConstantConditions", "unused"})   // 去掉idea的警告
public class BotEventHandler extends SimpleListenerHost {

  private final TarotService tarotService;
  private final BotDicService botDicService;
  private final PlayerService playerService;
  private final BotChatService botChatService;
  private final RedisCertUtils redisCertUtils;
  private final RedisChatUtils redisChatUtils;
  private final GalleryService galleryService;
  private final CookieLibService cookieLibService;
  private final GroupConfigService groupConfigService;
  private final SystemConfigService systemConfigService;
  private final BotFunctionWordService botFunctionWordService;

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
    }
  }

  /**
   * 处理群消息
   */
  @EventHandler
  private void handleGroupMsg(@NotNull GroupMessageEvent event) {
    Group group = event.getGroup();
    Bot bot = event.getBot();
    Long senderId = event.getSender().getId();
    String msg = event.getMessage().serializeToMiraiCode();
    MessageChainBuilder msgBuilder = new MessageChainBuilder();
    log.info("【群消息】收到 {} - {}({}) 的消息: \n{}", group.getName(), event.getSenderName(), senderId, msg);

    if (redisChatUtils.isUnSign(senderId)) {
      synchronized (senderId.toString().intern()) {
        if (redisChatUtils.isUnSign(senderId)) {
          handlePlayerSign(senderId, event.getSender().getNick());
        }
      }
    }

    // 取消生气状态   是否主人                        不传入发送人直接查                         是否包含机器人名称
    if (BotConst.QQ.equals(senderId) && checkBotAngry(bot, group.getId(), null, false) && msg.contains(BotConst.NAME)) {
      boolean flg = false;
      for (String word : BotConst.CANCEL_ANGRY) {
        if (msg.contains(word)) {
          redisChatUtils.cancelAngry(group.getId());
          flg = true;
          List<String> words = botFunctionWordService.getWords(6);
          checkImgMsg(words.get(CommonUtils.RANDOM.nextInt(words.size())), group, msgBuilder);
          group.sendMessage(msgBuilder.build());
        }
      }
      if (flg) {
        return;
      }
    }

    // 生气了吗.jpg          机器人被禁言中
    if (checkBotAngry(bot, group.getId(), senderId, false) || group.getBotMuteRemaining() > 0) {
      return;
    }

    // @/回复机器人
    if (msg.contains(BotConst.AT + bot.getId())) {
      msgBuilder.append(new At(senderId));
      handleAt(bot, group, msg, msgBuilder, senderId, event.getSenderName());
      return;
    }

    // 指令
    if (msg.startsWith(SEPARATOR2)) {
      handleCmd(event, msg.replace(SEPARATOR2, ""), msgBuilder);
      return;
    }

//    // 语句中包含关键字 检索聊天数据库
//    for (String keyword : BotConst.AWAKE_KEYWORD) {
//      if (msg.contains(keyword)) {
//        // TODO 查询聊天库语句回复
//      }
//    }

    // 记录文本, 复读
    if (BotConst.REPEAT_MODE) {
      String groupCode = String.valueOf(group.getId());
      // 冷却中直接结束 双校验防并发
      if (redisChatUtils.isRepeatCooling(groupCode)) {
        return;
      }
      synchronized (msg.intern()) {
        String prevMsg = redisChatUtils.getRepeatRecord(groupCode);
        // 未处于冷却状态
        if (!redisChatUtils.isRepeatCooling(groupCode)) {
          if (msg.equals(prevMsg)) {
            // 打断复读
            if (CommonUtils.RANDOM.nextInt(3) == 0) {
              List<String> words = botFunctionWordService.getWords(9);
              msgBuilder.append(words.get(CommonUtils.RANDOM.nextInt(words.size())));
            } else {
              msgBuilder.append(MiraiCode.deserializeMiraiCode(msg));
              redisChatUtils.setRepeatCooling(groupCode);   // 设置冷却
            }
            group.sendMessage(msgBuilder.build());   // 复读
            msg = "";  // 重置上一条聊天记录
          }
          redisChatUtils.setRepeatRecord(groupCode, msg);   // 保存聊天记录, 留作下次复读比照
        }
      }
    }
  }

  /**
   * 处理活跃状态
   */
  private void handlePlayerSign(Long qq, String nickname) {
    Player player = playerService.getPlayer(qq);
    if (null == player) {
      player = new Player(qq, nickname);
    }
    long signTime = System.currentTimeMillis() / 1000;
    if (signTime - player.getLastSignTime() > 86400) {
      player.setSeriesSign(1L);
    } else {
      player.setSeriesSign(player.getSeriesSign() + 1);
    }
    player.setLastSignTime(signTime);
    player.setIntegral(player.getIntegral() + 1);
    player.setTotalSign(player.getTotalSign() + 1);
    if (null == player.getId()) {
      playerService.save(player);
    } else {
      playerService.updateById(player);
    }
    redisChatUtils.setSignState(qq);
  }

  /**
   * 处理群戳一戳
   */
  @EventHandler
  private void handleGroupNudge(@NotNull NudgeEvent event) {
    Bot bot = event.getBot();
    Long botId = bot.getId();
    UserOrBot from = event.getFrom();
    UserOrBot target = event.getTarget();
    long groupId = event.getSubject().getId();
    log.info("【戳一戳】{}({}) {} {}({})", from.getNick(), from.getId(), event.getAction(), target.getNick(), target.getId());

    //         校验是否戳机器人                           生气了吗.jpg                         机器人被禁言中
    if (!botId.equals(target.getId()) || checkBotAngry(bot, groupId, from.getId(), true) || redisChatUtils.isBotMuted(groupId)) {
      return;
    }

    List<String> replyList = botFunctionWordService.getWords(0);
    int size = replyList.size();
    int i = CommonUtils.RANDOM.nextInt(size + 2);
    MessageChainBuilder msgBuilder = new MessageChainBuilder();

    try {
      // 群号id event.getSubject().getId()
      if (i == size) {  // 禁言
        mute(bot, groupId, from.getId(), "食我沉默啦！");
        return;
      } else if (i > size) {  // 戳回去
        nudge(null, groupId, from.getId(), bot);
        msgBuilder.append("戳回去！");
      } else {
        String reply = replyList.get(i);
        if (BotConst.WILL_ANGRY.equals(reply)) {  // 是否要生气了
          redisChatUtils.setWillAngryFlag(String.valueOf(groupId));
        } else if (reply.contains(BotConst.IS_ANGRY)) {   // 生气 禁言 不标记生气
          mute(bot, groupId, from.getId(), BotConst.NAME + reply);
          return;
        }
        msgBuilder.append(reply);
      }
      bot.getGroup(groupId).sendMessage(msgBuilder.build());
    } catch (Exception e) {
      log.error("【戳一戳】处理戳一戳消息失败, 堆栈信息: ", e);
    }
  }

  /**
   * 机器人被禁言事件
   */
  @EventHandler
  private void muteBotEvent(@NotNull BotMuteEvent event) {
    Group group = event.getGroup();
    redisChatUtils.setBotMuteState(group.getId(), event.getDurationSeconds());
  }

  /**
   * 机器人解除禁言事件
   */
  @EventHandler
  private void unMuteBotEvent(@NotNull BotUnmuteEvent event) {
    Group group = event.getGroup();
    redisChatUtils.delBotMuteState(group.getId());
    group.sendMessage(new MessageChainBuilder().append("好耶！").append(BotConst.NAME).append("复活！").build());
  }

  /**
   * 机器人被@处理
   */
  private void handleAt(Bot bot, Group group, String msg, MessageChainBuilder msgBuilder, Long senderId, String senderName) {
//    String reply = botDicService.queryTypeToGetWord(msg);
    String msgText = msg.split(BotConst.AT_END)[1].trim();
    if (StringUtils.isBlank(msgText)) {
      List<String> words = botFunctionWordService.getWords(7);
      String reply = words.get(CommonUtils.RANDOM.nextInt(words.size()));
      checkImgMsg(reply, group, msgBuilder);
    } else {
      // 暂时只处理图片和文本
      String reply = MoLiUtils.getReply(bot, msgText, senderId, senderName);
      if (reply.contains(APIConst.PROTOCAL)) {
        try {
          msgBuilder.append(Contact.uploadImage(group, ServletUtils.get(reply).getContent()));
        } catch (Exception e) {
          log.error("【茉莉API】解析图片地址失败, 堆栈信息: ", e);
          msgBuilder.append("解析回复的图片地址失败.....");
        }
      } else {
        msgBuilder.append(reply);
      }
    }
    group.sendMessage(msgBuilder.build());
  }

  /**
   * 指令处理
   *
   * @date 2021-08-24 16:04
   */
  private void handleCmd(GroupMessageEvent event, String msgText, MessageChainBuilder msgBuilder) {
    Member sender = event.getSender();
    Group group = event.getGroup();
    String[] msgArr = msgText.split(SEPARATOR);
    switch (msgArr[0]) {
      // 指令列表
      case CMD_LIST -> msgBuilder.append("""
          #随机图片@原神@刻晴|凝光
          #随机涩图@jk|少女@女仆|猫耳
          #账号查询@101010101
          #查号@101010101
          #查号2(此指令为B服)@101010101
          #丘丘语(未完成)@gusha
          #每日塔罗
          #参数配置@key@val@常量标记(可缺省)

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
      case RANDOM_IMAGE -> handleRandomImage(event.getSender().getId(), msgText, group, msgBuilder);
      case UPDATE_GALLERY -> msgBuilder.append("已更换为pixiv插画收集，此指令已 Deprecated");
      case QIU_QIU_TRANSLATION -> msgBuilder.append("小奏还没有学会丘丘语翻译呢");
      case RANDOM_SEXY -> handleRandomSexy(event, msgBuilder, msgArr);
      case NO_MOLE -> handleR18(event, msgBuilder, msgArr);
      case DAILY_TAROT -> handleDailyTarot(event, msgBuilder);
      case CONFIGURATION -> handleConfiguration(event, msgBuilder);
      case LITTLE_BLACK_HOUSE -> {    // 关小黑屋
        try {
          if (!BotConst.QQ.equals(sender.getId()) && sender.getPermission().getLevel() == 0) {
            sender.mute(10);    // 禁言10s
            msgBuilder.append(new PlainText("你在教我做事？"));
          } else {
            String muteId = msgArr[1].split(BotConst.AT_END)[0].replace(BotConst.AT, "");
            if (StringUtils.isNotBlank(muteId) && msgArr.length >= 3) {
              group.get(Long.parseLong(muteId)).mute(Integer.parseInt(msgArr[2]) * 60);
            }
          }
          msgBuilder.append(Contact.uploadImage(group, new File(BotConst.IMG_GS)));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
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
      return "图片功能冷却中！！还剩" + ((coolTime - System.currentTimeMillis()) / 1000) + "秒";
    }
    return null;
  }

  /**
   * 随机涩图处理逻辑 无R18
   */
  private void handleRandomSexy(GroupMessageEvent groupMsgEvent, MessageChainBuilder builder, String[] msgArr) {
    Long memberCode = groupMsgEvent.getSender().getId();
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

      // 校验禁止词
      boolean forbid = false;
      for (String s : msgArr) {
        if (ProjectConst.FORBID_WORD.contains(s)) {
          builder.append("不可以色色！不可以色色！");
          forbid = true;
          break;
        }
      }
      if (forbid) {
        return;
      }


      List<String> params = new ArrayList<>(Arrays.asList(msgArr));
      params.remove(0);
      List<LoLiConResult> results = ServletUtils.handleLoLiConReq(MapperUtils.serialize(new LoLiConReq(0, 1, params)));
      if (results.size() <= 0) {
        builder.append("再怎么找也找不到的啦，换一张吧");
        return;
      }

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
      if (!BotConst.QQ.equals(memberCode)) {
        redisCertUtils.setRandomImgCoolTime(code);
      }
    }
  }

  /**
   * 获取R18图
   */
  private void handleR18(GroupMessageEvent event, MessageChainBuilder msgBuilder, String[] msgArr) {
    if (!BotConst.QQ.equals(event.getSender().getId())) {
      msgBuilder.append("无内鬼也不给你!");
      return;
    }
    // TODO
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

  /**
   * 传入群号/群对象禁言某群员
   */
  private void mute(Bot bot, Long groupId, Long memberId, String originMsg) {
    try {
      Group group = bot.getGroup(groupId);
      if (null == group) {
        log.error("【禁言】机器人不存在群 {}, 禁言失败!", groupId);
      }
      NormalMember member = group.get(memberId);
      MessageChainBuilder msgBuilder = new MessageChainBuilder();
      switch (member.getPermission().getLevel()) {
        case 0 -> {
          member.mute(CommonUtils.RANDOM.nextInt(541) + 60);
          msgBuilder.append(originMsg);
        }
        case 1 -> msgBuilder.append(CommonUtils.RANDOM.nextInt(2) == 0 ? "哼, 看在是管理的份上原谅你, 才不是因为禁言不了你呢!" : "这次先放过你了！");
        case 2 -> msgBuilder.append(BotConst.QQ.equals(memberId) ? "主人" : "群主").append("的话可以哦");
      }
      group.sendMessage(msgBuilder.build());
    } catch (Exception e) {
      log.error("【禁言】用户 {} 不在群中 or 机器人权限不足, 禁言失败, 堆栈信息: ", memberId, e);
    }
  }

  /**
   * 传入群号/群对象戳群员
   */
  public void nudge(Group group, Long groupId, Long memberId, Bot bot) {
    try {
      group = null == group ? bot.getGroup(groupId) : group;
      group.get(memberId).nudge().sendTo(group);
    } catch (Exception e) {
      log.error("【戳一戳】机器人不在群 {} 中或用户 {} 不在群中, 戳一戳失败", null == groupId ? group.getId() : groupId, memberId);
    }
  }

  /**
   * 校验机器人是否生气
   *
   * @param memberId 戳一戳的来源方/群聊消息的发送方
   */
  private boolean checkBotAngry(Bot bot, Long groupId, Long memberId, boolean isNudge) {
    try {
      String angryFlag = redisChatUtils.getWillAngryFlag(groupId);
      // 校验机器人生气状态
      if (!BotConst.QQ.equals(memberId)) {
        if (ProjectConst.ONE.equals(angryFlag)) {  // 已经生气
          return true;
        }
        if (isNudge && ProjectConst.ZERO.equals(angryFlag)) { // 开始生气 不再处理戳一戳
          redisChatUtils.setAngry(groupId);
          Group group = bot.getGroup(groupId);
          MessageChainBuilder builder = new MessageChainBuilder().append("生气气！")
              .append(Contact.uploadImage(group, new File("/home/littleKanade/imgSource/mamalielie.jpg")));
          group.sendMessage(builder.build());
          return true;
        }
      }
    } catch (Exception e) {
      log.error("【校验生气】校验/设置 机器人生气状态失败, 堆栈信息: ", e);
    }
    return false;
  }

  /**
   * 系统参数配置
   */
  private void handleConfiguration(GroupMessageEvent event, MessageChainBuilder msgBuilder) {
    if (!BotConst.QQ.equals(event.getSender().getId())) {
      msgBuilder.append("再怎么发你也没权限设置的啦~");
      return;
    }
    String[] params = event.getMessage().contentToString().split(SEPARATOR);
    int length = params.length;

    if (length < 3 || length > 4) {
      msgBuilder.append("指令有误: #参数配置@key@value@常量标记(可缺省)");
      return;
    }
    systemConfigService.updateVal(params[1], params[2]);
    if (length == 4) {  // 需要读取配置到常量
      BotConfig.setConfig(systemConfigService.list());
    }
    msgBuilder.append("配置已更新完成，注意测试是否配置成功");
  }

  /**
   * 判断是否图片路径字符串
   */
  public static void checkImgMsg(String word, Group group, MessageChainBuilder msgBuilder) {
    if (word.contains(ProjectConst.PNG) || word.contains(ProjectConst.JPG)) {
      msgBuilder.append(Contact.uploadImage(group, new File(word)));
    } else {
      msgBuilder.append(word.replace(ProjectConst.REPLACE_CHAR, BotConst.NAME));
    }
  }

  /**
   * 随机图片（图库获取）
   */
  private void handleRandomImage(Long memberCode, String msgText, Group group, MessageChainBuilder msgBuilder) {
    String groupId = String.valueOf(group.getId());
    int sexyState = groupConfigService.getGroupSexy(groupId);

    if (!BotConst.QQ.equals(memberCode)) {
      String result = handleCheckCooling(groupId);
      if (null != result) {
        msgBuilder.append(result);
        return;
      }
    }

    synchronized (groupId.intern()) {
      if (!BotConst.QQ.equals(memberCode)) {
        String result = handleCheckCooling(groupId);
        if (null != result) {
          msgBuilder.append(result);
          return;
        }
      }
      // redisCertUtils.setRandomImgCoolTime(groupId);
    }

    List<String> params = null;
    String[] mainParams = msgText.replaceAll(RANDOM_IMAGE, "").split(SEPARATOR);
    if (mainParams.length > 1) {
      params = new ArrayList<>(Arrays.asList(mainParams));
      params.remove(0);
    }
    String path = galleryService.randomImg(sexyState, params);
    if (null != path) {
      msgBuilder.append(Contact.uploadImage(group, new File(path)));
      redisCertUtils.setRandomImgCoolTime(groupId);
    } else {
      msgBuilder.append("再怎么找也找不到的啦，还不快邦邦入库");
    }
  }

}
