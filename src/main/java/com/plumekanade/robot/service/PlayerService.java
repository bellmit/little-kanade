package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.Player;
import com.plumekanade.robot.mapper.PlayerMapper;
import org.springframework.stereotype.Service;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-04-27 10:04:12
 */
@Service
public class PlayerService extends ServiceImpl<PlayerMapper, Player> {

  /**
   * 根据qq获取对应用户
   */
  public Player getPlayer(Long qq) {
    return getOne(new LambdaQueryWrapper<Player>().eq(Player::getCode, qq));
  }
}
