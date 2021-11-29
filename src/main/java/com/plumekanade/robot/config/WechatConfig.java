package com.plumekanade.robot.config;

import com.plumekanade.robot.handler.WechatMsgHandler;
import com.plumekanade.robot.handler.WechatScanHandler;
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

import static me.chanjar.weixin.common.api.WxConsts.XmlMsgType.EVENT;

/**
 * 微信配置类
 *
 * @author kanade
 * @date 2021-10-28 10:53
 */
@Configuration
public class WechatConfig {

  private final SysConfig sysConfig;
  private final WechatMsgHandler wechatMsgHandler;
  private final WechatScanHandler wechatScanHandler;

  private static final String WECHAT = "wechat";

  @Resource(name = "redisZero")
  private JedisPool zero;

  public WechatConfig(SysConfig sysConfig, WechatMsgHandler wechatMsgHandler, WechatScanHandler wechatScanHandler) {
    this.sysConfig = sysConfig;
    this.wechatMsgHandler = wechatMsgHandler;
    this.wechatScanHandler = wechatScanHandler;
  }

  @Bean
  public WxMpService wxMpService() {
    WxMpService service = new WxMpServiceImpl();
    WxMpDefaultConfigImpl configStorage = new WxMpRedisConfigImpl(new JedisWxRedisOps(zero), WECHAT);
    configStorage.setAppId(sysConfig.getWxAppId());
    configStorage.setSecret(sysConfig.getWxAppSecret());
    configStorage.setToken(sysConfig.getWxAppToken());
    configStorage.setAesKey(sysConfig.getWxAesKey());
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
