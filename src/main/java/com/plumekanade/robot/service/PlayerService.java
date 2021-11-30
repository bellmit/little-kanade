package com.plumekanade.robot.service;

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
}
