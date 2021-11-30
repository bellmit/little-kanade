package com.plumekanade.robot.utils;

//import com.kanade.bot.constant.ProjectConst;
//import com.kanade.bot.vo.TextProcessResult;
//import com.tencentcloudapi.common.Credential;
//import com.tencentcloudapi.nlp.v20190408.NlpClient;
//import com.tencentcloudapi.nlp.v20190408.models.ChatBotRequest;
//import com.tencentcloudapi.tbp.v20190627.TbpClient;
//import com.tencentcloudapi.tbp.v20190627.models.Group;
//import com.tencentcloudapi.tbp.v20190627.models.TextProcessRequest;
//import com.tencentcloudapi.tbp.v20190627.models.TextProcessResponse;
import lombok.extern.slf4j.Slf4j;
//
//import java.util.Arrays;

/**
 * 腾讯NLP工具类
 *
 * @author kanade
 * @date 2021-11-28 16:10
 */
@Slf4j
public class TencentNlpUtils {

//  public static Credential cred = null;
//  public static NlpClient nlpClient;
//  public static String botId;
//  public static TbpClient tbpClient;

//  /**
//   * 通用闲聊
//   */
//  public static String chat(String word) {
//
//    // 实例化一个请求对象,每个接口都会对应一个request对象
//    ChatBotRequest req = new ChatBotRequest();
//    req.setQuery(word);
//    try {
//      // 返回的是一个ChatBotResponse的实例，与请求对象对应
//      return nlpClient.ChatBot(req).getReply();
//    } catch (Exception e) {
//      log.error("【NLP】对话接口获取失败, 堆栈信息: ", e);
//    }
//    return null;
//  }
//
//  /**
//   * 智能对话平台bot聊天
//   * <p>
//   * {"DialogStatus":"",
//   * "BotName":"",
//   * "IntentName":"",
//   * "SlotInfoList":[],
//   * "InputText":"绝了",
//   * "ResponseMessage":
//   * {"GroupList":
//   * [
//   * {
//   * "ContentType":"text/plain",
//   * "Url":"",
//   * "Content":"呵呵,好厉害哦。"
//   * }
//   * ]
//   * },
//   * "SessionAttributes":"",
//   * "ResultType":"3",
//   * "RequestId":"b96f009d-602c-470f-85bc-15c398b2f826"
//   * }
//   * </p>
//   *
//   * @param accountCode 用户q号/用户标识
//   * @param word        话语
//   */
//  public static String botChat(String accountCode, String word) {
//    try {
//      // 实例化一个请求对象,每个接口都会对应一个request对象
//      TextProcessRequest req = new TextProcessRequest();
//      req.setBotId(botId);
//      req.setBotEnv(ProjectConst.RELEASE);
//      req.setTerminalId(accountCode);
//      req.setInputText(word);
//      // 返回的是一个TextProcessResponse的实例，与请求对象对应
//      Group[] groupList = tbpClient.TextProcess(req).getResponseMessage().getGroupList();
//      if (groupList.length > 0) {
//        TextProcessResult result = MapperUtils.deserialize(MapperUtils.serialize(groupList[0]), TextProcessResult.class);
//        if (null != result) {
//          return result.getContent();
//        }
//      }
//    } catch (Exception e) {
//      log.error("【智能对话平台】获取对话结果失败, 堆栈信息: ", e);
//    }
//    return null;
//  }

}
