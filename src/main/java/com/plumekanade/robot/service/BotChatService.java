package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.BotChat;
import com.plumekanade.robot.mapper.BotChatMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @version 1.0
 * @author kanade
 * @date 2021-11-30 16:11
 */
@Service
@AllArgsConstructor
public class BotChatService extends ServiceImpl<BotChatMapper, BotChat> {

  private final BotChatMapper botChatMapper;

  /**
   * 获取被戳一戳时
   */
  public List<String> getNudges() {
    return botChatMapper.getNudges();
  }
}
