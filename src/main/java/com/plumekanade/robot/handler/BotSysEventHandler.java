package com.plumekanade.robot.handler;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.events.*;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * 系统事件处理器
 * @version 1.0
 * @author kanade
 * @date 2021-12-31 09:09
 */
@Slf4j
@Component
@AllArgsConstructor
@SuppressWarnings({"unused"})   // 去掉idea的警告
public class BotSysEventHandler {

  /**
   * 有人申请加群
   */
  @EventHandler
  private void memberJoinGroupRequest(@NotNull MemberJoinRequestEvent event) {
    log.info("有人申请加群, 群: {}({}), 申请人: {}({})", event.getGroupName(), event.getGroupId(), event.getFromNick(), event.getFromId());
    event.accept();
  }

  /**
   * 被邀请进入一个群
   */
  @EventHandler
  private void joinGroup(@NotNull BotJoinGroupEvent event) {
    log.info("成功加入一个新群: {}", event.getGroup());
  }

  /**
   * 有新成员加入
   */
  @EventHandler
  private void memberJoinGroup(@NotNull MemberJoinEvent event) {
    log.info("新成员成功加入, 群: {}, 新成员: {}", event.getGroup(), event.getMember());
    event.getGroup().sendMessage(new MessageChainBuilder().append("是新大佬！带带！").build());
  }

  /**
   * 成员离开群
   */
  @EventHandler
  private void memberLeaveGroup(@NotNull MemberLeaveEvent.Quit event) {
    log.info("成员已离开, 群: {}, 成员: {}", event.getGroup(), event.getMember());
  }

  /**
   * 被邀请加入一个群
   */
  @EventHandler
  private void invitedJoinGroup(@NotNull BotInvitedJoinGroupRequestEvent event) {
    log.info("被邀请加入群: {}({}), 邀请人: {}", event.getGroupName(), event.getGroupId(), event.getInvitor());
  }

}
