package com.plumekanade.robot.vo;

import com.plumekanade.robot.constants.APIConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 图片索引请求VO(随机色图)
 * 文档地址: https://api.lolicon.app/#/setu
 * @author kanade
 * @date 2021-11-24 0:05
 */
@Data
@NoArgsConstructor
public class LoLiConReq implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * 0为非 R18，1为 R18，2为混合（在库中的分类，不等同于作品本身的 R18 标识） 默认0
   */
  private int r18 = 0;
  /**
   * 一次返回的结果数量，范围为1到100；在指定关键字或标签的情况下，结果数量可能会不足指定的数量
   */
  private int num = 1;
  /**
   * 返回指定uid作者的作品，最多20个
   */
  private List<Integer> uid;
  /**
   * 返回从标题、作者、标签中按指定关键字模糊匹配的结果，大小写不敏感，性能和准度较差且功能单一，建议使用tag代替
   */
  private String keyword;
  /**
   * 返回匹配指定标签的作品 字符串列表/数组
   */
  private List<String> tag;
  /**
   * 返回指定图片规格的地址 默认普通图 original regular
   */
  private String[] size = {"regular"};
  /**
   * 设置图片地址所使用的在线反代服务
   */
  private String proxy = APIConst.IMG_PROXY_DOMAIN;
  /**
   * 返回在这个时间及以后上传的作品；时间戳，单位为毫秒
   */
  private Integer dateAfter;
  /**
   * 返回在这个时间及以前上传的作品；时间戳，单位为毫秒
   */
  private Integer dateBefore;
  /**
   * 设置为任意真值以禁用对某些缩写keyword和tag的自动转换
   */
  private Boolean dsc;

  public LoLiConReq(int r18, int num, List<String> tag) {
    this.r18 = r18;
    this.num = num;
    this.tag = tag;
  }
}
