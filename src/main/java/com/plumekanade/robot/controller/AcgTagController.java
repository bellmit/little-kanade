package com.plumekanade.robot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plumekanade.robot.entity.AcgTag;
import com.plumekanade.robot.service.AcgTagService;
import com.plumekanade.robot.vo.ResultMsg;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @version 1.0
 * @author kanade
 * @date 2022-01-21 15:16
 */
@RestController
@AllArgsConstructor
@RequestMapping("/acgTag")
public class AcgTagController {

  private final AcgTagService acgTagService;

  /**
   * 分页列表
   */
  @GetMapping("/getPage")
  public ResultMsg getPage(@RequestParam("page") int page, @RequestParam("size") int size,
                           @RequestParam(value = "name", required = false) String name) {
    LambdaQueryWrapper<AcgTag> wrapper = new LambdaQueryWrapper<>();
    wrapper.orderByDesc(AcgTag::getCreateTime);
    if (StringUtils.isNotBlank(name)) {
      wrapper.like(AcgTag::getName, name);
    }
    return ResultMsg.success(acgTagService.page(new Page<>(page, size), wrapper));
  }

  /**
   * 添加/修改
   */
  @PostMapping("/dataSave")
  public ResultMsg dataSave(@RequestBody AcgTag acgTag) {
    if (null != acgTag.getId()) {
      acgTagService.updateById(acgTag);
    } else {
      acgTagService.save(acgTag);
    }
    return ResultMsg.success();
  }

  /**
   * 删除
   */
  @PostMapping("/delete")
  public ResultMsg delete(@RequestBody AcgTag acgTag) {
    return ResultMsg.success("" + acgTagService.removeById(acgTag));
  }

}
