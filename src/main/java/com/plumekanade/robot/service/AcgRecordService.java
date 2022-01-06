package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.AcgRecord;
import com.plumekanade.robot.mapper.AcgRecordMapper;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-10-22 16:54
 */
@Service
@AllArgsConstructor
public class AcgRecordService extends ServiceImpl<AcgRecordMapper, AcgRecord> {

  private final AcgRecordMapper acgRecordMapper;

  /**
   * 模糊查询某动画
   */
  public List<AcgRecord> getAnimations(String acgName) {
    LambdaQueryWrapper<AcgRecord> wrapper = new LambdaQueryWrapper<>();
    wrapper.like(AcgRecord::getAcgName, acgName).eq(AcgRecord::getType, 0).orderByDesc(AcgRecord::getCreateTime);
    return list(wrapper);
  }

  /**
   * 模糊查询某漫画
   */
  public List<AcgRecord> getComics(String acgName) {
    LambdaQueryWrapper<AcgRecord> wrapper = new LambdaQueryWrapper<>();
    wrapper.like(AcgRecord::getAcgName, acgName).eq(AcgRecord::getType, 1).orderByDesc(AcgRecord::getCreateTime);
    return list(wrapper);
  }

  /**
   * 模糊查询某动画
   */
  public List<AcgRecord> getNovels(String acgName) {
    LambdaQueryWrapper<AcgRecord> wrapper = new LambdaQueryWrapper<>();
    wrapper.like(AcgRecord::getAcgName, acgName).eq(AcgRecord::getType, 2).orderByDesc(AcgRecord::getCreateTime);
    return list(wrapper);
  }

  /**
   * 获取未看完的所有动画
   *
   * @param quarter 动画季度, 如: 202107、202010
   */
  public List<AcgRecord> getAllAnimations(String quarter, Integer state) {
    LambdaQueryWrapper<AcgRecord> wrapper = new LambdaQueryWrapper<>();
    wrapper.eq(AcgRecord::getType, 0).orderByDesc(AcgRecord::getCreateTime);
    if (null != state) {
      wrapper.eq(AcgRecord::getState, state);
    }
    if (StringUtils.isNotBlank(quarter)) {
      wrapper.eq(AcgRecord::getQuarter, quarter);
    }
    return list(wrapper);
  }

  /**
   * 更改番剧/小说/漫画状态
   */
  public void updateStateWithTitle(String title, String state) {
    acgRecordMapper.updateStateWithTitle(title, state);
  }
}
