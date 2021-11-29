package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 世界探索信息
 *
 * @author kanade
 * @version 1.0
 * @date 2021-07-29 17:15
 */
@Data
@JsonPropertyOrder(alphabetic = true)
public class GenshinWorldInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  // 声望等级
  private Integer level;
  // 探索度 1000为100%  982为98.2%
  private Integer exploration_percentage;
  // 图标
  private String icon;
  // 名称
  private String name;
  // 目前出现 Reputation 和 Offering 两种
  private String type;
  // 如果type为 Offering 的时候 此处不为空
  private List<Offer> offerings;


  @Data
  public static class Offer implements Serializable {

    private static final long serialVersionUID = 1L;

    // 奉献物品名称 如忍冬之树
    private String name;
    // 奉献等级
    private Integer level;

  }

}
