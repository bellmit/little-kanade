package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 图库（指令触发）
 *
 * @author kanade
 * @version 1.0
 * @date 2021-08-24 14:22
 */
@Data
@NoArgsConstructor
@TableName("gallery")
public class Gallery implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  // 图片原名称(废弃)
  @Deprecated
  private String originName;
  // 图片名称  作者_标题_id_p0.png
  private String filename;
  // 图片模式 0普通 1涩 2露点
  private int sexyState;
  // 文件路径
  private String path;
  // 独立访问url
  private String url;
  // 插画标签 逗号隔开
  private String tags;
  // 插画作者
  private String author;
  // 插画原图（并非服务器的图片大小）大小 存放到服务器一律压缩到最大 2560×1440 / 1920×1080
  private Long size;
  // p站插画id
  private String pixivId;
  // 标题
  private String title;
  // 插画上传的时间(作者上传时间)
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date uploadTime;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

  public Gallery(String originName, String filename, int sexyState, String path, String url) {
    this.url = url;
    this.path = path;
    this.filename = filename;
    this.sexyState = sexyState;
    this.originName = originName;
  }

  public Gallery(String title, String filename, String pixivId, String author, int sexyState, String path, String url, String tags, Date uploadTime) {
    this.url = url;
    this.path = path;
    this.tags = tags;
    this.title = title;
    this.author = author;
    this.pixivId = pixivId;
    this.filename = filename;
    this.sexyState = sexyState;
    this.uploadTime = uploadTime;
  }

}
