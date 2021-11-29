package com.plumekanade.robot.handler;

import com.plumekanade.robot.utils.MapperUtils;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 扫码事件
 *
 * @author kanade
 * @date 2021-10-28 11:34
 */
@Component
public class WechatScanHandler implements WxMpMessageHandler {
  @Override
  public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage, Map<String, Object> map, WxMpService wxMpService,
                                  WxSessionManager wxSessionManager) throws WxErrorException {
//        if (!wxMessage.getMsgType().equals(WxConsts.XmlMsgType.EVENT)) {
//            // 可以选择将消息保存到本地
//        }

//        //当用户输入关键词如“你好”，“客服”等，并且有客服在线时，把消息转发给在线客服
//        try {
//            if (StringUtils.startsWithAny(wxMessage.getContent(), "你好", "客服") &&
//                    wxMpService.getKefuService().kfOnlineList().getKfOnlineList().size() > 0) {
//                return WxMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().fromUser(wxMessage.getToUser())
//                        .toUser(wxMessage.getFromUser()).build();
//            }
//        } catch (WxErrorException e) {
//            e.printStackTrace();
//        }

    // 组装回复消息
    String content = "收到信息内容：" + MapperUtils.serialize(wxMessage);

    return WxMpXmlOutMessage.TEXT().content(content).fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser()).build();
  }
}
