package com.plumekanade.robot.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.code.MiraiCode;
import net.mamoe.mirai.message.data.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Objects;

/**
 * @author kanade
 * @date 2021-11-29 23:15
 */
@Slf4j
@Component
@AllArgsConstructor
public class BotEventHandler extends SimpleListenerHost {

  /**
   * 处理私聊消息
   */
  @EventHandler
  public void handlePrivateMsg(@NotNull FriendMessageEvent event) {
    Friend friend = event.getFriend();
    log.info("【私聊】收到 {} - {} 的消息: {}", friend.getId(), friend.getNick(), MessageChain.serializeToJsonString(event.getMessage()));
    friend.sendMessage("看到伊蕾娜了吗" + MiraiCode.serializeToMiraiCode(Contact.uploadImage(friend, new File("D:\\download\\致伊蕾娜_87135864_p1.jpg"))));
  }

  /**
   * 处理群消息
   */
  @EventHandler
  private void handleGroupMsg(@NotNull GroupMessageEvent event) {
    Group group = event.getGroup();
    MessageChain message = event.getMessage();
    Member member = event.getSender();
    message.get(MessageContent.Key);
//    log.info("【群消息】收到 {} - {}({}) 的消息: {}", group.getName(), event.getSenderName(), member.getId(), MessageChain.serializeToJsonString(message));
    log.info("【测试消息】" + message.contentToString());
    log.info("【测试消息】" + Objects.requireNonNull(message.get(Image.Key)).getImageId());
  }

}
