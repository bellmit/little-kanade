package com.plumekanade.robot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plumekanade.robot.entity.SystemConfig;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @version 1.0
 * @author kanade
 * @date 2021-11-27 15:10:08
 */
@Repository
public interface SystemConfigMapper extends BaseMapper<SystemConfig> {

  /**
   * 获取所有配置 以param的值作为val的键名组成JSON字符串
   */
  @Select("""
      select group_concat(concat('"', param, '":"', val, '"')) as json from system_config order by id""")
  String getMapVal();
}
