package com.plumekanade.robot.utils;

import com.plumekanade.robot.constants.APIConst;
import com.plumekanade.robot.constants.BotConst;
import com.plumekanade.robot.vo.MoLiParam;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.List;


/**
 * 茉莉API工具类
 *
 * @author kanade
 * @version 1.0
 * @date 2022-01-13 16:01:46
 */
@Slf4j
public class MoLiUtils {

  private static final String API_KEY = "Api-Key";
  private static final String API_SECRET = "Api-Secret";
  private static final String SUCCESS_CODE = "00000";

  /**
   * 获取机器人回复
   */
  public static String getReply(Bot bot, String content, Long qq, String nickname) {
    Header[] headers = new Header[2];
    headers[0] = new BasicHeader(API_KEY, APIConst.MO_LI_KEY);
    headers[1] = new BasicHeader(API_SECRET, APIConst.MO_LI_SECRET);
    MoLiParam.Request param = new MoLiParam.Request(content, qq, nickname, bot.getId(), BotConst.NAME);
    try {
      MoLiParam.Result result = MapperUtils.deserialize(
          ServletUtils.post(APIConst.MO_LI, MapperUtils.serialize(param), headers), MoLiParam.Result.class);
      if (SUCCESS_CODE.equals(result.getCode())) {
        List<MoLiParam.ResultItem> data = result.getData();
        if (data.size() > 0) {
          MoLiParam.ResultItem resultItem = data.get(0);
          if (resultItem.getTyped() == 1) {
            return resultItem.getContent();
          } else if (resultItem.getTyped() == 2){
            return APIConst.MO_LI_FILE + resultItem.getContent();
          } else {
            return ".....\n.....\n诶嘿";
          }
        }
      }
    } catch (Exception e) {
      log.error("【茉莉API】获取茉莉API接口回复异常, 堆栈信息: ", e);
      return "机器人接口不太对劲√";
    }
    return "机器人接口怪怪的（";
  }

}
