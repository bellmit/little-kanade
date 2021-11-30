package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.constants.SysKeyConst;
import com.plumekanade.robot.entity.CookieLib;
import com.plumekanade.robot.mapper.CookieLibMapper;
import org.springframework.stereotype.Service;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-05-17 16:41:07
 */
@Service
public class CookieLibService extends ServiceImpl<CookieLibMapper, CookieLib> {

  /**
   * 根据q号获取对应游戏cookie
   *
   * @date 2021-08-24 16:18
   */
  public CookieLib getCookie(Long qq) {
    return getOne(new QueryWrapper<CookieLib>().eq(SysKeyConst.QQ, qq));
  }

  /**
   * 根据游戏角色id获取对应的cookie记录
   */
  public CookieLib getByYsId(String ysId) {
    return getOne(new LambdaQueryWrapper<CookieLib>().eq(CookieLib::getYsId, ysId));
  }

}
