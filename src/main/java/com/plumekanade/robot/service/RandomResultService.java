package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.RandomResult;
import com.plumekanade.robot.mapper.RandomResultMapper;
import org.springframework.stereotype.Service;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-04-27 10:05:12
 */
@Service
public class RandomResultService extends ServiceImpl<RandomResultMapper, RandomResult> {
}
