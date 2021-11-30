package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.SystemConfig;
import com.plumekanade.robot.mapper.SystemConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @version 1.0
 * @author kanade
 * @date 2021-11-27 15:10:38
 */
@Service
public class SystemConfigService extends ServiceImpl<SystemConfigMapper, SystemConfig> {

  /**
   * 根据param字段获取对应的val
   */
  public String getVal(String param) {
    return getOne(new LambdaQueryWrapper<SystemConfig>().eq(SystemConfig::getParam, param)).getVal();
  }

  /**
   * 根据param模糊查询val列表
   */
  public List<SystemConfig> getLikeValList(String param) {
    return list(new LambdaQueryWrapper<SystemConfig>().like(SystemConfig::getParam, param).orderByAsc(SystemConfig::getId));
  }

  /**
   * 根据param修改val
   */
  public void setVal(String param, String val) {
    update(new LambdaUpdateWrapper<SystemConfig>().set(SystemConfig::getVal, val).eq(SystemConfig::getParam, param));
  }
}
