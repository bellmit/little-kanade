package com.plumekanade.robot.utils;

import com.plumekanade.robot.constants.APIConst;
import com.plumekanade.robot.constants.ProjectConst;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 茉莉API工具类
 *
 * @author kanade
 * @version 1.0
 * @date 2021-04-26 15:15:16
 */
@Slf4j
public class MoLiUtils {

  /**
   * 笑话
   */
  public static String joke() {
    // TODO 后续改为随机错误语句
    String result = "emmmm";
    Map<String, String> resultMap = ServletUtils.convertGet(APIConst.MO_LI_API + ProjectConst.JOKE);
    if (resultMap.size() > 0) {
      log.info("【笑话】 {}", MapperUtils.serialize(resultMap));
    }
    return result;
  }

  /**
   * 月老灵签
   */
  public static String yueLao() {
    // TODO 后续改为随机错误语句
    String result = "emmmm";
    Map<String, String> resultMap = ServletUtils.convertGet(APIConst.MO_LI_API + ProjectConst.YUE_LAO_LOT);
    if (resultMap.size() > 0) {
      log.info("【月老灵签】 {}", MapperUtils.serialize(resultMap));
    }
    return result;
  }

  /**
   * 观音灵签
   */
  public static String guanYin() {
    // TODO 后续改为随机错误语句
    String result = "emmmm";
    Map<String, String> resultMap = ServletUtils.convertGet(APIConst.MO_LI_API + ProjectConst.GUAN_YIN_LOT);
    if (resultMap.size() > 0) {
      log.info("【观音灵签】 {}", MapperUtils.serialize(resultMap));
    }
    return result;
  }

  /**
   * 财神爷灵签
   */
  public static String caiShenYe() {
    // TODO 后续改为随机错误语句
    String result = "emmmm";
    Map<String, String> resultMap = ServletUtils.convertGet(APIConst.MO_LI_API + ProjectConst.CAI_SHEN_LOT);
    if (resultMap.size() > 0) {
      log.info("【财神爷灵签】 {}", MapperUtils.serialize(resultMap));
    }
    return result;
  }

  /**
   * 格式化灵签API数据
   */
  public static String formatLot(Map<String, String> map) {
    return "";
  }

}
