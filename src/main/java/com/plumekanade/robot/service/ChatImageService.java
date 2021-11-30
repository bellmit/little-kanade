package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.ChatImage;
import com.plumekanade.robot.mapper.ChatImageMapper;
import org.springframework.stereotype.Service;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-08-24 14:14
 */
@Service
public class ChatImageService extends ServiceImpl<ChatImageMapper, ChatImage> {

  private final ChatImageMapper chatImageMapper;

  public ChatImageService(ChatImageMapper chatImageMapper) {
    this.chatImageMapper = chatImageMapper;
  }

  /**
   * 根据图片哈希值获取图片记录
   *
   * @date 2021-08-24 15:14
   */
  public ChatImage getImage(String hash) {
    return chatImageMapper.selectOne(new QueryWrapper<ChatImage>().eq("hash", hash));
  }
}
