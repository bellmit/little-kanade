package com.plumekanade.robot.config;

import com.plumekanade.robot.constants.SysKeyConst;
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

import java.util.Map;

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
  public static String OPEN_ID;
  public static String ORIGIN_ID;

  @Resource(name = "redisZero")
  private JedisPool zero;

  public WechatConfig(WechatMsgHandler wechatMsgHandler, WechatScanHandler wechatScanHandler, SystemConfigService systemConfigService) {
    this.wechatMsgHandler = wechatMsgHandler;
    this.wechatScanHandler = wechatScanHandler;
    this.systemConfigService = systemConfigService;
  }

  @Bean
  public WxMpService wxMpService() {
    Map<String, String> wxMapVal = systemConfigService.getMapVal("wx");
    OPEN_ID = wxMapVal.get(SysKeyConst.OPENID);
    ORIGIN_ID = wxMapVal.get(SysKeyConst.WX_ORIGIN_ID);
    WxMpService service = new WxMpServiceImpl();
    WxMpDefaultConfigImpl configStorage = new WxMpRedisConfigImpl(new JedisWxRedisOps(zero), WECHAT);
    configStorage.setAppId(wxMapVal.get(SysKeyConst.WX_APP_ID));
    configStorage.setSecret(wxMapVal.get(SysKeyConst.WX_APP_SECRET));
    configStorage.setToken(wxMapVal.get(SysKeyConst.WX_APP_TOKEN));
    configStorage.setAesKey(wxMapVal.get(SysKeyConst.WX_AES_KEY));
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
