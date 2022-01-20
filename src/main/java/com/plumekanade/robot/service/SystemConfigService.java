package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.type.TypeReference;
import com.plumekanade.robot.entity.SystemConfig;
import com.plumekanade.robot.mapper.SystemConfigMapper;
import com.plumekanade.robot.utils.MapperUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-11-27 15:10:38
 */
@Service
@AllArgsConstructor
public class SystemConfigService extends ServiceImpl<SystemConfigMapper, SystemConfig> {

  private final SystemConfigMapper systemConfigMapper;

  /**
   * 根据param字段获取对应的val
   */
  public String getVal(String param) {
    return getOne(new LambdaQueryWrapper<SystemConfig>().eq(SystemConfig::getParam, param)).getVal();
  }

  /**
   * 根据param修改val
   */
  public void setVal(String param, String val) {
    update(new LambdaUpdateWrapper<SystemConfig>().set(SystemConfig::getVal, val).eq(SystemConfig::getParam, param));
  }

  /**
   * 获取所有配置 以param的值作为val的键名
   */
  public Map<String, String> getMapVal(String query) {
    LambdaQueryWrapper<SystemConfig> wrapper = new LambdaQueryWrapper<>();
    if (StringUtils.isNotBlank(query)) {
      wrapper.like(SystemConfig::getParam, query);
    }
    return MapperUtils.deserialize("{" + systemConfigMapper.getMapVal(wrapper) + "}", new TypeReference<>() {
    });
  }

}
