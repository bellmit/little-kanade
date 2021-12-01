package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.GroupConfig;
import com.plumekanade.robot.mapper.GroupConfigMapper;
import com.plumekanade.robot.utils.MapperUtils;
import com.plumekanade.robot.utils.RedisCertUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-08-27 15:11
 */
@Service
@AllArgsConstructor
public class GroupConfigService extends ServiceImpl<GroupConfigMapper, GroupConfig> {

  private final RedisCertUtils redisCertUtils;

  /**
   * 根据群号获取涩图等级
   */
  public int getGroupSexy(String groupId) {
    GroupConfig groupConfig = redisCertUtils.getGroupConfig(groupId);
    if (null == groupConfig) {
      groupConfig = getOne(new LambdaQueryWrapper<GroupConfig>().eq(GroupConfig::getGroupCode, groupId));
      redisCertUtils.setGroupConfig(groupId, MapperUtils.serialize(groupConfig));
    }
    return null == groupConfig ? 0 : groupConfig.getSexyState();
  }

}
