package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.GroupConfig;
import com.plumekanade.robot.mapper.GroupConfigMapper;
import org.springframework.stereotype.Service;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-08-27 15:11
 */
@Service
public class GroupConfigService extends ServiceImpl<GroupConfigMapper, GroupConfig> {

  private final GroupConfigMapper groupConfigMapper;

  public GroupConfigService(GroupConfigMapper groupConfigMapper) {
    this.groupConfigMapper = groupConfigMapper;
  }

  /**
   * 根据群号获取配置信息
   *
   * @date 2021-08-27 15:12
   */
  public GroupConfig getConfig(String groupCode) {
    return groupConfigMapper.selectOne(new QueryWrapper<GroupConfig>().eq("group_code", groupCode));
  }

}
