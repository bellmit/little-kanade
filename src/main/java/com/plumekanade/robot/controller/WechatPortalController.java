package com.plumekanade.robot.controller;

import com.plumekanade.robot.config.WechatConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutTextMessage;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * 微信服务器入口
 *
 * @author kanade
 * @date 2021-10-28 11:50
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/wechat")
public class WechatPortalController {

  private final WxMpService wxMpService;
  //  private final RedisCertUtils redisCertUtils;
  private final WxMpMessageRouter messageRouter;

  @GetMapping(value = "/portal/{appId}", produces = "text/plain;charset=utf-8")
  public String portalGet(@PathVariable String appId, @RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {

    log.info("\n接收到来自微信服务器的认证消息：[{}, {}, {}, {}]", signature, timestamp, nonce, echostr);
    if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
      throw new IllegalArgumentException("请求参数非法，请核实!");
    }

    if (!wxMpService.switchover(appId)) {
      throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appId));
    }

    if (wxMpService.checkSignature(timestamp, nonce, signature)) {
      return echostr;
    }

    return "非法请求";
  }

  @PostMapping(value = "/portal/{appId}", produces = "application/xml; charset=UTF-8")
  public String portalPost(@PathVariable String appId, @RequestBody String requestBody,
                           @RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp,
                           @RequestParam("nonce") String nonce, @RequestParam("openid") String openid,
                           @RequestParam(name = "encrypt_type", required = false) String encType,
                           @RequestParam(name = "msg_signature", required = false) String msgSignature) {
    if (!wxMpService.switchover(appId)) {
      throw new IllegalArgumentException(String.format("未找到对应appid=[%s]的配置，请核实！", appId));
    }
    if (!wxMpService.checkSignature(timestamp, nonce, signature)) {
      throw new IllegalArgumentException("非法请求，可能属于伪造的请求！");
    }

    String wxUserName = "管理员微信";
    WxMpConfigStorage wxMpConfigStorage = wxMpService.getWxMpConfigStorage();

    // 暂时只管自己的微信
    if (!WechatConfig.OPEN_ID.equals(openid)) {
      WxMpXmlOutTextMessage wxOutMessage = WxMpXmlOutMessage.TEXT().content("您好, 暂不响应管理员微信以外消息")
          .fromUser(WechatConfig.ORIGIN_ID).toUser(openid).build();
      return wxOutMessage.toEncryptedXml(wxMpConfigStorage);
//      wxUserName = redisCertUtils.getWxUserName(openid);
//      if (StringUtils.isBlank(wxUserName)) {
//        // 从数据库中获取用户昵称
//      }
    }

    String replyMsg = null;
    if (encType == null) {
      // 明文传输的消息
      replyMsg = route(WxMpXmlMessage.fromXml(requestBody)).toXml();
    } else if ("AES".equalsIgnoreCase(encType)) {
      // AES加密的消息
      WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(requestBody, wxMpConfigStorage, timestamp, nonce, msgSignature);
      String content = inMessage.getContent();
      log.info("【微信消息】用户 {} 发送消息内容: {}, 图片内容: {}", wxUserName, content, inMessage.getSendPicsInfo());
      replyMsg = route(inMessage).toEncryptedXml(wxMpConfigStorage);
    }
    return replyMsg;
  }

  private WxMpXmlOutMessage route(WxMpXmlMessage message) {
    try {
      return messageRouter.route(message);
    } catch (Exception e) {
      log.error("路由消息时出现异常！", e);
    }
    return WxMpXmlOutMessage.TEXT().content("服务器异常, 请稍后再试").fromUser(message.getToUser()).toUser(message.getFromUser()).build();
  }

}
