package com.plumekanade.robot.utils;

import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.vo.PixivArtwork;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static com.plumekanade.robot.constants.PixivConst.*;

/**
 * P站相关处理工具类
 *
 * @author kanade
 * @version 1.0
 * @date 2021-12-08 10:53
 */
@Slf4j
public class PixivUtils {

  /**
   * 解析插画页面
   *
   * @return 如果解析失败则返回失败原因 成功返回null
   */
  public static PixivArtwork.Illust parseArtWork(Document document, String artworkId) throws Exception {

    Element element = document.getElementById(META_PRELOAD_DATA);
    if (null == element) {
      throw new Exception("找不到 " + META_PRELOAD_DATA + " 的meta标签对象");
    }
    Attributes attributes = element.attributes();
    for (Attribute attribute : attributes) {
      if (CONTENT.equals(attribute.getKey())) {
        PixivArtwork artwork = MapperUtils.deserialize(attribute.getValue(), PixivArtwork.class);
        if (null != artwork) {
          return artwork.getIllust().get(artworkId);
        }
      }
    }
    throw new Exception("序列化/反序列化后数据为空！");
  }

  /**
   * 根据插画地址获取插画id
   */
  public static String getArtworkId(String url) {
    String[] arr = url.split(ProjectConst.SLASH);
    return arr[arr.length - 1];
  }

}
