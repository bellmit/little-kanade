package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * P站图片解析VO
 * 示例 -> doc/pixiv-preload-data.json
 *
 * @author kanade
 * @version 1.0
 * @date 2021-12-08 12:01
 */
@Data
public class PixivArtwork implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  private Map<String, Illust> illust;

  @Data
  public static class Illust implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 插画描述？
    private String alt;
    // 收藏数
    private Integer bookmarkCount;
    // 评论数
    private Integer commentCount;
    // 插画上传时间 默认为国际标准时区(GMT+0)
    @JsonFormat(pattern = DateConst.PIXIV_DT, timezone = DateConst.PIXIV_TZ)
    private Date createDate;
    // 额外数据
    private ExtraData extraData;
    // 高
    private Integer height;
    // 插画id
    private String id;
    // 插画说明？
    private String illustComment;
    // 插画id
    private String illustId;
    // 插画标题
    private String illustTitle;
    // 插画类型 不知道有啥
    private Integer illustType;
    // 喜欢数
    private Integer likeCount;
    // 标签列表
    private Tags tags;
    // 插画标题
    private String title;
    // 插画上传时间
    @JsonFormat(pattern = DateConst.PIXIV_DT, timezone = DateConst.PIXIV_TZ)
    private Date uploadDate;
    // 插画直链url地址
    private Urls urls;
    // 作者名称
    private String userName;
    // 插画被查看数
    private Integer viewCount;
    // 宽
    private Integer width;
  }


  /**
   * 额外数据
   */
  @Data
  public static class ExtraData implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 描述
    private String description;
    // 描述头
    private String descriptionHeader;

  }

  /**
   * 标签列表
   */
  @Data
  public static class Tags implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 作者id
    private String authorId;
    // 是否锁定
    private boolean isLocked;
    // 标签列表
    private List<Tag> tags;
    // 是否可编辑？
    private boolean writable;
  }

  /**
   * 标签VO
   */
  @Data
  public static class Tag implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 是否可删除
    private boolean deletable;
    // 是否锁定
    private boolean locked;
    // 标签名称
    private String tag;
    // 标签的其他翻译 例: {"en":"Genshin Impact"}
    private Map<String, String> translation;
    // 用户id
    private String userId;
    // 用户名称
    private String userName;
  }

  /**
   * 插图直链地址VO
   */
  @Data
  public static class Urls implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    // 迷你图 48*48
    private String mini;
    // 原图
    private String original;
    // 标准图
    private String regular;
    // 小图 540*540 0.7
    private String small;
    // 缩略图 250*250 0.8
    private String thumb;
  }

}
