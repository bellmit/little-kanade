package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;

/**
 * 暂只有尘歌壶的洞天
 *
 * @author kanade
 * @version 1.0
 * @date 2021-07-29 17:37
 */
@Data
@JsonPropertyOrder(alphabetic = true)
public class GenshinHome implements Serializable {
  private static final long serialVersionUID = 1L;

  // 信任等级
  private Integer level;
  // 历史访客数
  private Integer visit_num;
  // 洞天最高仙力(舒适度)
  private Integer comfort_num;
  // 获得摆设数
  private Integer item_num;
  // 洞天名称
  private String name;
  // 洞天图片
  private String icon;
  // 仙力(舒适度)等级名称
  private String comfort_level_name;
  // 仙力(舒适度)等级图片
  private String comfort_level_icon;

}
