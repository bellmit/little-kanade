package com.plumekanade.robot.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 请求返回结果VO
 * <p>
 * {
 * "code":0,
 * "msg":"",
 * "count":1,
 * "data":[
 * {
 * "pid":89767155,
 * "p":0,
 * "uid":4792800,
 * "title":"銀髪ドレス",
 * "author":"鈴雨やつみ",
 * "r18":false,
 * "width":1000,
 * "height":1414,
 * "tags":[
 * "オリジナル",
 * "原创",
 * "銀髪",
 * "银发",
 * "ドレス",
 * "裙子",
 * "魅惑のふともも",
 * "魅惑的大腿",
 * "ボディチェーン",
 * "身体链",
 * "足組み",
 * "跷二郎腿",
 * "腋",
 * "腋下",
 * "胸ポチ",
 * "凸点",
 * "銀髪赤眼",
 * "silver hair and red eyes"
 * ],
 * "url":"https://i.acgmx.com/img-original/img/2021/05/11/17/18/14/89767155_p0.jpg"
 * }
 * ]
 * }
 * </p>
 *
 * @author kanade
 * @version 1.0
 * @date 2021-11-24 09:18:20
 */
@Data
public class LoLiConResult implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * 作品uid
   */
  private Long pid;
  /**
   * 跟随在作品id后面的p几 表示该作品的第几张 如: 123456789_p0.jpg 123456789_p1.jpg
   */
  private int p;
  /**
   * 作者uid
   */
  private Long uid;
  /**
   * 作品标题
   */
  private String title;
  /**
   * 作者名称
   */
  private String author;
  /**
   * 是否r18
   */
  private boolean r18;
  /**
   * 作品分辨率 宽
   */
  private int width;
  /**
   * 作品分辨率 高
   */
  private int height;
  /**
   * 标签列表
   */
  private List<String> tags;
  /**
   * 文件格式
   */
  private String ext;
  /**
   * 上传时间
   */
  private Long uploadDate;
  /**
   * url地址
   */
  private URLS urls;

  @Data
  public static class URLS implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 原图url地址
     */
    private String original;
    /**
     * 原图url地址
     */
    private String regular;
  }

}
