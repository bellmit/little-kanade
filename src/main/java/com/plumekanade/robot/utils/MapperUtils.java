package com.plumekanade.robot.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.plumekanade.robot.constants.DateConst;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * 序列化/反序列化工具类
 *
 * @author kanade
 * @version 1.0
 * @date 2021-01-21 09:41
 */
@Slf4j
public class MapperUtils {

  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    // 在序列化时日期格式默认为 yyyy-MM-dd HH:mm:ss
    mapper.getSerializationConfig().with(DateConst.SDF);
    // 在序列化时忽略值为 null 的属性
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    // 指定序列化字段顺序
    mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
    // 在反序列化时忽略在 json 中存在但 Java 对象不存在的属性
    mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    // 格式化输出
    mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
    // 忽略无法转换的对象
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
  }

  /**
   * 将实体类对象转换成json字符串
   */
  public static String serialize(Object object) {

    try {
      if (null != object) {
        return mapper.writeValueAsString(object);
      }
    } catch (Exception e) {
      log.error("Jackson转换String异常: ", e);
    }
    return null;
  }

  /**
   * 反序列化(带有unicode)
   */
  public static <T> T deserialize(InputStream in, TypeReference<T> typeReference) {
    try {
      return mapper.readValue(in, typeReference);
    } catch (Exception e) {
      log.error("Jackson转换String异常: ", e);
    }
    return null;
  }

  /**
   * 将json字符串转换成对应的实体类对象
   */
  public static <T> T deserialize(String json, Class<T> tClass) {
    try {
      if (StringUtils.isNotBlank(json)) {
        return mapper.readValue(json, tClass);
      }
    } catch (Exception e) {
      log.error("Jackson转换 {} 类异常: ", tClass, e);
    }
    return null;
  }

  /**
   * 将json字符串转换成对应的实体类对象
   */
  public static <T> T deserialize(String json, TypeReference<T> typeReference) {
    try {
      if (StringUtils.isNotBlank(json)) {
        return mapper.readValue(json, typeReference);
      }
    } catch (Exception e) {
      log.error("Jackson转换 {} 类异常: ", typeReference.getClass(), e);
    }
    return null;
  }

}
