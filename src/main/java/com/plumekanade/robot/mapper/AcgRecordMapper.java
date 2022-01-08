package com.plumekanade.robot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plumekanade.robot.entity.AcgRecord;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-10-22 16:54
 */
@Repository
public interface AcgRecordMapper extends BaseMapper<AcgRecord> {

  /**
   * 更改ACG状态
   */
  @Update("update acg_record set state = #{state} where acg_name = #{name}")
  void updateStateWithName(String name, String state);
}
