package com.plumekanade.robot.controller;

import com.plumekanade.robot.constants.PixivConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.entity.Gallery;
import com.plumekanade.robot.service.GalleryService;
import com.plumekanade.robot.utils.CommonUtils;
import com.plumekanade.robot.utils.PixivUtils;
import com.plumekanade.robot.utils.ServletUtils;
import com.plumekanade.robot.vo.PixivArtwork;
import com.plumekanade.robot.vo.ResultMsg;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.message.BasicHeader;
import org.jsoup.Jsoup;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.COMMA;
import static com.baomidou.mybatisplus.core.toolkit.StringPool.UNDERSCORE;


/**
 * P站解析
 *
 * @author kanade
 * @version 1.0
 * @date 2021-12-08 10:54
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/pixivParse")
public class PixivParseController {

  private final GalleryService galleryService;

  /**
   * 解析单插画作品(可能多图片)
   * https://www.pixiv.net/artworks/94611329
   * @param url https://plumekanade.com/pixivParse/artwork?sexy=1&num=1&url=https://www.pixiv.net/artworks/94611329
   *
   * @param sexy 涩图等级 0普通 1涩 2露点
   * @param num  该插画作品有几张图
   * @param originSave 是否保存原图 0否 1是
   */
  @GetMapping("/artwork")
  @Transactional(rollbackFor = Exception.class)
  public ResultMsg item(@RequestParam("url") String url, @RequestParam(value = "sexy", required = false, defaultValue = "0") Integer sexy,
                        @RequestParam(value = "num", required = false, defaultValue = "1") Integer num,
                        @RequestParam(value = "originSave", defaultValue = "0") Integer originSave) {
    try {
      String pixivId = PixivUtils.getArtworkId(url);
      PixivArtwork.Illust illust = PixivUtils.parseArtWork(Jsoup.connect(url).get(), pixivId);
      StringBuilder tagBuilder = new StringBuilder();
      for (PixivArtwork.Tag tag : illust.getTags().getTags()) {
        tagBuilder.append(tag.getTag()).append(COMMA);
        if (null != tag.getTranslation()) {
          for (String value : tag.getTranslation().values()) {
            tagBuilder.append(value).append(COMMA);
          }
        }
      }

      String tags = tagBuilder.substring(0, tagBuilder.length() - 1);
      String originUrl = illust.getUrls().getOriginal();
      String path = sexy == 0 ? ProjectConst.NORMAL_GALLERY_PATH : (sexy == 1 ? ProjectConst.SEXY_GALLERY_PATH : ProjectConst.BARE_GALLERY_PATH);
      // 防路径插入
      String filename = (illust.getUserName() + UNDERSCORE + illust.getTitle() + UNDERSCORE + pixivId + UNDERSCORE + "p").replaceAll("/", "");
      String galleryUrl = ProjectConst.GALLERY_URL + (sexy == 0 ? "normal/" : (sexy == 1 ? "sexy/" : "bare/"));

      url = originUrl;
      Header header = new BasicHeader(PixivConst.REFERER_KEY, PixivConst.REFERER_VAL);
      String p0 = "p0", jpg = ".jpg", png = ".png";
      for (int i = 0; i < num; i++) {
        // png压缩效果不明显 基本压缩不了多少
        String name = filename + i + jpg;
        if (i > 0) {
          url = originUrl.replaceAll(p0 + jpg, "p" + i + jpg).replaceAll(p0 + png, "p" + i + png);
        }

        HttpEntity httpEntity = ServletUtils.getWithHeader(url, header);
        if (null == httpEntity) {
          log.error("【Pixiv】图片url: " + url);
          return ResultMsg.error("获取图片失败，请查看是否能连接到 https://pixiv.net/");
        }

        File file = new File(path + name);
        // 原图是否存入本地
        if (null != originSave && 1 == originSave) {
          String originPath = ProjectConst.ORIGIN_PATH + name.replaceAll(".jpg", originUrl.substring(originUrl.length() - 4));
          CommonUtils.writeFile(httpEntity.getContent(), originPath);
          Thumbnails.of(new File(originPath)).size(2560, 2560).outputQuality(0.9f).toFile(file);
        } else {
          // 压缩
          Thumbnails.of(httpEntity.getContent()).size(2560, 2560).outputQuality(0.9f).toFile(file);
        }
        Gallery gallery = new Gallery(illust.getTitle(), name, pixivId, illust.getUserName(), sexy,
            file.getPath(), galleryUrl + name, tags, illust.getUploadDate());
        gallery.setSize(file.length());
        Gallery existGallery = galleryService.getImage(gallery.getPath());
        if (null != existGallery) {
          gallery.setId(existGallery.getId());
          galleryService.updateById(gallery);
        } else {
          galleryService.save(gallery);
        }
      }
    } catch (Exception e) {
      log.error("【Pixiv】解析P站指定插画页面失败, 地址: {}, 堆栈信息: ", url, e);
      return ResultMsg.error(e.getMessage());
    }
    return ResultMsg.success();
  }

}
