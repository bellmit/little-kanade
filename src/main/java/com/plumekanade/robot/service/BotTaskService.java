package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.BotTask;
import com.plumekanade.robot.mapper.BotTaskMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author kanade
 * @date 2021-11-07 13:32
 */
@Service
public class BotTaskService extends ServiceImpl<BotTaskMapper, BotTask> {

  /**
   * 获取未过期/结束的任务列表
   */
  public List<BotTask> getActiveList(String targetName) {
    LambdaQueryWrapper<BotTask> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(BotTask::getState, 1).le(BotTask::getStartTime, new Date());
    if (StringUtils.isNotBlank(targetName)) {
      wrapper.like(BotTask::getName, targetName);
    }
    return list(wrapper);
  }

  /**
   * 根据标题查找对应的执行中任务 不模糊查询
   */
  public BotTask getActiveTask(String targetName) {
    LambdaQueryWrapper<BotTask> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(BotTask::getName, targetName);
    wrapper.eq(BotTask::getState, 1);
    return getOne(wrapper);
  }
}
