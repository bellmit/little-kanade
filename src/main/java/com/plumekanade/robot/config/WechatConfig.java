package com.plumekanade.robot.config;

import com.plumekanade.robot.entity.SystemConfig;
import com.plumekanade.robot.handler.WechatMsgHandler;
import com.plumekanade.robot.handler.WechatScanHandler;
import com.plumekanade.robot.service.SystemConfigService;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.redis.JedisWxRedisOps;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import me.chanjar.weixin.mp.config.impl.WxMpRedisConfigImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;

import java.util.List;

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;

/**
 * 微信配置类
 *
 * @author kanade
 * @date 2021-10-28 10:53
 */
@Configuration
public class WechatConfig {

  private final WechatMsgHandler wechatMsgHandler;
  private final WechatScanHandler wechatScanHandler;
  private final SystemConfigService systemConfigService;

  private static final String WECHAT = "wechat";

  @Resource(name = "redisZero")
  private JedisPool zero;

  public WechatConfig(WechatMsgHandler wechatMsgHandler, WechatScanHandler wechatScanHandler, SystemConfigService systemConfigService) {
    this.wechatMsgHandler = wechatMsgHandler;
    this.wechatScanHandler = wechatScanHandler;
    this.systemConfigService = systemConfigService;
  }

  @Bean
  public WxMpService wxMpService() {
    List<SystemConfig> list = systemConfigService.getLikeValList("wx");
    WxMpService service = new WxMpServiceImpl();
    WxMpDefaultConfigImpl configStorage = new WxMpRedisConfigImpl(new JedisWxRedisOps(zero), WECHAT);
    configStorage.setAppId(list.get(0).getVal());
    configStorage.setSecret(list.get(1).getVal());
    configStorage.setToken(list.get(2).getVal());
    configStorage.setAesKey(list.get(3).getVal());
    service.setWxMpConfigStorage(configStorage);
    return service;
  }

  @Bean
  public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
    WxMpMessageRouter router = new WxMpMessageRouter(wxMpService);
    // 扫码事件
    router.rule().async(false).msgType(EVENT).event(WxConsts.EventType.SCAN).handler(wechatScanHandler).end();
    // 普通消息事件
    router.rule().async(false).handler(wechatMsgHandler).end();
    return router;
  }
}
