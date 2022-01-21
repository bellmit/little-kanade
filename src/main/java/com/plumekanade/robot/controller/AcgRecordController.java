package com.plumekanade.robot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plumekanade.robot.entity.AcgRecord;
import com.plumekanade.robot.entity.DelLog;
import com.plumekanade.robot.service.AcgRecordService;
import com.plumekanade.robot.service.DelLogService;
import com.plumekanade.robot.utils.MapperUtils;
import com.plumekanade.robot.vo.ResultMsg;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @version 1.0
 * @author kanade
 * @date 2022-01-21 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping("/acg")
public class AcgRecordController {

  private final DelLogService delLogService;
  private final AcgRecordService acgRecordService;

  /**
   * 获取分页列表
   * @param search 名称(acgName) / 所属季度(quarter)
   */
  @GetMapping("/getPage")
  public ResultMsg getPage(@RequestParam(value = "page") int page, @RequestParam("size") int size,
                           @RequestParam(value = "search", required = false) String search,
                           @RequestParam(value = "type", required = false) Integer type,
                           @RequestParam(value = "state", required = false) Integer state) {
    LambdaQueryWrapper<AcgRecord> wrapper = new LambdaQueryWrapper<>();
    wrapper.orderByDesc(AcgRecord::getCreateTime);
    if (StringUtils.isNotBlank(search)) {
      wrapper.and(w -> w.like(AcgRecord::getAcgName, search).or().eq(AcgRecord::getQuarter, search));
    }
    if (null != type) {
      wrapper.eq(AcgRecord::getType, type);
    }
    if (null != state) {
      wrapper.eq(AcgRecord::getState, state);
    }
    return ResultMsg.success(acgRecordService.page(new Page<>(page, size), wrapper));
  }

  /**
   * 添加/修改
   */
  @PostMapping("/dataSave")
  public ResultMsg dataSave(@RequestBody AcgRecord acgRecord) {
    if (null != acgRecord.getId()) {
      acgRecordService.updateById(acgRecord);
    } else {
      acgRecordService.save(acgRecord);
    }
    return ResultMsg.success();
  }

  /**
   * 删除
   */
  @PostMapping("/delete")
  public ResultMsg delete(@RequestBody AcgRecord acgRecord) {
    acgRecord = acgRecordService.getById(acgRecord.getId());
    // 保存删除记录
    delLogService.save(new DelLog("AcgRecord", MapperUtils.serialize(acgRecord)));
    return ResultMsg.error("" + acgRecordService.removeById(acgRecord.getId()));
  }

}
