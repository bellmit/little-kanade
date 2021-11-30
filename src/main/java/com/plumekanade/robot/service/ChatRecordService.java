package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.ChatRecord;
import com.plumekanade.robot.mapper.ChatRecordMapper;
import org.springframework.stereotype.Service;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-04-27 10:02:35
 */
@Service
public class ChatRecordService extends ServiceImpl<ChatRecordMapper, ChatRecord> {
}
