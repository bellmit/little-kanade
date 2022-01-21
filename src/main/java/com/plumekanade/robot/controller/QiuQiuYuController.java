package com.plumekanade.robot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plumekanade.robot.entity.DelLog;
import com.plumekanade.robot.entity.QiuQiuTranslate;
import com.plumekanade.robot.service.DelLogService;
import com.plumekanade.robot.service.QiuQiuTranslateService;
import com.plumekanade.robot.utils.MapperUtils;
import com.plumekanade.robot.vo.ResultMsg;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 丘丘语翻译
 * @version 1.0
 * @author kanade
 * @date 2022-01-21 15:40
 */
@RestController
@AllArgsConstructor
@RequestMapping("/qqy")
public class QiuQiuYuController {

  private final DelLogService delLogService;
  private final QiuQiuTranslateService qiuQiuTranslateService;

  /**
   * 分页列表
   * @param qqy 丘丘语
   * @param translation 译文
   */
  @GetMapping("/getPage")
  public ResultMsg getPage(@RequestParam("page") int page, @RequestParam("size") int size,
                           @RequestParam(value = "qqy", required = false) String qqy,
                           @RequestParam(value = "translation", required = false) String translation) {
    LambdaQueryWrapper<QiuQiuTranslate> wrapper = new LambdaQueryWrapper<>();
    wrapper.orderByDesc(QiuQiuTranslate::getCreateTime);
    if (StringUtils.isNotBlank(qqy)) {
      wrapper.like(QiuQiuTranslate::getQiuQiuLanguage, qqy);
    }
    if (StringUtils.isNotBlank(translation)) {
      wrapper.like(QiuQiuTranslate::getTranslation, translation);
    }
    return ResultMsg.success(qiuQiuTranslateService.page(new Page<>(page, size), wrapper));
  }

  /**
   * 添加/修改
   */
  @PostMapping("/dataSave")
  public ResultMsg dataSave(@RequestBody QiuQiuTranslate qiuQiuTranslate) {
    if (null != qiuQiuTranslate.getId()) {
      qiuQiuTranslateService.updateById(qiuQiuTranslate);
    } else {
      qiuQiuTranslateService.save(qiuQiuTranslate);
    }
    return ResultMsg.success();
  }

  /**
   * 删除
   */
  @PostMapping("/delete")
  public ResultMsg delete(@RequestBody QiuQiuTranslate qiuQiuTranslate) {
    qiuQiuTranslate = qiuQiuTranslateService.getById(qiuQiuTranslate.getId());
    delLogService.save(new DelLog("QiuQiuTranslate", MapperUtils.serialize(qiuQiuTranslate)));
    return ResultMsg.success("" + qiuQiuTranslateService.removeById(qiuQiuTranslate.getId()));
  }

}
