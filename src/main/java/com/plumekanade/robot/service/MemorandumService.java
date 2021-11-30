package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.Memorandum;
import com.plumekanade.robot.mapper.MemorandumMapper;
import org.springframework.stereotype.Service;

/**
 * @author kanade
 * @date 2021-11-14 22:49
 */
@Service
public class MemorandumService extends ServiceImpl<MemorandumMapper, Memorandum> {
}
