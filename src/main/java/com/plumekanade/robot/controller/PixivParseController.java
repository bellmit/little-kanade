package com.plumekanade.robot.controller;

import com.plumekanade.robot.utils.PixivUtils;
import com.plumekanade.robot.vo.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;


/**
 * P站解析
 * @version 1.0
 * @author kanade
 * @date 2021-12-08 10:54
 */
@Slf4j
@RestController
@RequestMapping("/pixivParse")
public class PixivParseController {

  /**
   * 解析单插画作品(可能多图片)
   * https://www.pixiv.net/artworks/94611329
   */
  @GetMapping("/artwork")
  public ResultMsg item(@RequestParam("url") String url) {
    String result;
    try {
      result = PixivUtils.parseArtWork(Jsoup.connect(url).get());
      if (null == result) {
        return ResultMsg.success();
      }
    } catch (Exception e) {
      log.error("【Pixiv】解析P站指定插画页面失败, 地址: {}, 堆栈信息: ", url, e);
      result = "解析插画页面失败！";
    }
    return ResultMsg.error(result);
  }

}
