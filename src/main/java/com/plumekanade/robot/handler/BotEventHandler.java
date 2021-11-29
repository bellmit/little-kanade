package com.plumekanade.robot.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.contact.Friend;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.FriendMessageEvent;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.MessageChain;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * @author kanade
 * @date 2021-11-29 23:15
 */
@Slf4j
@Component
@AllArgsConstructor
public class BotEventHandler {

  /**
   * 处理私聊消息
   */
  public void handlePrivateMsg(@NotNull FriendMessageEvent event) {
    Friend friend = event.getFriend();
    log.info("【私聊】收到 {} - {} 的消息: {}", friend.getId(), friend.getNick(), MessageChain.serializeToJsonString(event.getMessage()));
  }

  /**
   * 处理群消息
   */
  @EventHandler
  private void handleGroupMsg(@NotNull GroupMessageEvent event) {
    Group group = event.getGroup();
    MessageChain message = event.getMessage();
    Member member = event.getSender();
    log.info("【群消息】收到 {} - {}({}) 的消息: {}", group.getName(), member.getNameCard(), member.getId(), MessageChain.serializeToJsonString(message));
    log.info("【测试群消息输出】" + message.serializeToMiraiCode());
  }

}
