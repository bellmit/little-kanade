package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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

  private final CookieLibMapper cookieLibMapper;

  public CookieLibService(CookieLibMapper cookieLibMapper) {
    this.cookieLibMapper = cookieLibMapper;
  }

  /**
   * 根据q号获取对应游戏cookie
   *
   * @date 2021-08-24 16:18
   */
  public CookieLib getCookie(String qq) {
    return cookieLibMapper.selectOne(new QueryWrapper<CookieLib>().eq("qq", qq));
  }
}
