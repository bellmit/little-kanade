package com.plumekanade.robot.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.vo.PixivArtwork;
import com.plumekanade.robot.vo.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static com.plumekanade.robot.constants.PixivConst.*;

/**
 * P站相关处理工具类
 * @version 1.0
 * @author kanade
 * @date 2021-12-08 10:53
 */
@Slf4j
public class PixivUtils {

  /**
   * 解析插画页面
   * @return 如果解析失败则返回失败原因 成功返回null
   */
  public static String parseArtWork(Document document) {

    Element element = document.getElementById(META_PRELOAD_DATA);
    if (null == element) {
      return "找不到 " + META_PRELOAD_DATA + " 的meta标签对象";
    }
    Attributes attributes = element.attributes();
    for (Attribute attribute : attributes) {
      if (CONTENT.equals(attribute.getKey())) {
        PixivArtwork artwork = MapperUtils.deserialize(attribute.getValue(), PixivArtwork.class);
        if (null != artwork) {
          MapperUtils.deserialize(MapperUtils.serialize(artwork.getIllust().get("94611329")), PixivArtwork.class);
          log.info("解析: " + MapperUtils.serialize(artwork));
        }
      }
    }

    return null;
  }

  /**
   * 根据插画地址获取插画id
   */
  public static String getArtworkId(String url) {
    String[] arr = url.split(ProjectConst.SLASH);
    return arr[arr.length - 1];
  }

  public static void main(String[] args) {
    try {
      String result = PixivUtils.parseArtWork(Jsoup.parse(new File("D:\\little-kanade\\doc\\artwork.html"), "UTF-8"));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 图片压缩
   */
  public void compressImg(InputStream in, String path) throws IOException {

    File file = new File(path);
    if (!file.getParentFile().exists()) { // 创建目录
      // noinspection ResultOfMethodCallIgnored
      file.getParentFile().mkdirs();
    }
    Thumbnails.of(in).size(2560, 1440)
        // .keepAspectRatio(false)   // 是否遵循原图比例 false不遵循
        // .scale(1f)        // 缩放
        .outputQuality(0.9f).toFile(file);
  }

}
