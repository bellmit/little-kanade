package com.plumekanade.robot.controller;

import com.plumekanade.robot.constants.AuthConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.entity.SystemUser;
import com.plumekanade.robot.enums.CodeEnum;
import com.plumekanade.robot.service.GalleryService;
import com.plumekanade.robot.service.SystemUserService;
import com.plumekanade.robot.utils.RedisCertUtils;
import com.plumekanade.robot.vo.ResultMsg;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

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
  private final RedisCertUtils redisCertUtils;
  private final SystemUserService systemUserService;

  /**
   * 压缩现有的大于2MB的图片
   */
  @GetMapping("/compressGallery")
  public ResultMsg compressGallery() {
    return ResultMsg.success(galleryService.compressImg());
  }

  /**
   * 登录
   */
  @PostMapping("/login")
  public ResultMsg login(@RequestBody SystemUser user) {
    if (StringUtils.isNotBlank(user.getUsername())) {
      return ResultMsg.error(CodeEnum.PWD_ERROR);
    }
    SystemUser dbUser = systemUserService.getById(ProjectConst.ONE);
    if (!AuthConst.PWD_ENCODER.matches(user.getPassword(), dbUser.getPassword())) {
      return ResultMsg.error(CodeEnum.PWD_ERROR);
    }

    String token = DigestUtils.md5DigestAsHex(String.valueOf(System.currentTimeMillis()).getBytes());
    redisCertUtils.setToken(token, dbUser);
    return ResultMsg.success(token);
  }

}
