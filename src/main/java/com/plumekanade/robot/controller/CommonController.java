package com.plumekanade.robot.controller;

import com.plumekanade.robot.service.GalleryService;
import com.plumekanade.robot.vo.ResultMsg;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 通用API
 * @version 1.0
 * @author kanade
 * @date 2021-12-31 11:09
 */
@RestController
@AllArgsConstructor
@RequestMapping("/common")
public class CommonController {

  private final GalleryService galleryService;

  /**
   * 压缩现有的大于2MB的图片
   */
  @GetMapping("/compressGallery")
  public ResultMsg compressGallery() {
    return ResultMsg.success(galleryService.compressImg());
  }

}
