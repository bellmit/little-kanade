package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 人物相关信息
 *
 * @author kanade
 * @version 1.0
 * @date 2021-08-07 13:41
 */
@Data
@JsonPropertyOrder(alphabetic = true)
public class GenshinCharacterInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  private Long id;
  // 图片
  private String image;
  // 图标
  private String icon;
  // 名称
  private String name;
  // 元素
  private String element;
  // 好感度
  private Integer fetter;
  // 等级
  private Integer level;
  // 星级
  private Integer rarity;
  // 武器
  private Weapon weapon;
  // 圣遗物 没有圣遗物时此处为空
  private List<Reliquary> reliquaries;
  // 命座
  private List<Constellation> constellations;
  // 激活命座数
  private Integer actived_constellation_num;
  // 服装?皮肤?
  private List<Object> costumes;


  /**
   * 武器
   */
  @Data
  public static class Weapon implements Serializable {
    
    private static final long serialVersionUID = 1L;
    private Long id;
    // 名称
    private String name;
    // 图片
    private String icon;
    // 类型
    private Integer type;
    // 星级
    private Integer rarity;
    // 武器等级
    private Integer level;
    // 突破等级
    private Integer promote_level;
    // 类型名称 如单手剑
    private String type_name;
    // 武器描述
    private String desc;
    // 精炼等级
    private Integer affix_level;
  }

  /**
   * 圣遗物
   */
  @Data
  public static class Reliquary implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    // 名称
    private String name;
    // 图片
    private String icon;
    // 位置 1花 2羽毛 3沙漏 4杯子 5头
    private Integer pos;
    // 位置名称
    private String pos_name;
    // 星级
    private Integer rarity;
    // 等级
    private Integer level;
//    // 套装效果 "id": 2150011,
//				"name": "角斗士的终幕礼",
//				"affixes": [{
//					"activation_number": 2,
//					"effect": "攻击力提高18%。"
//				}, {
//					"activation_number": 4,
//					"effect": "装备该圣遗物套装的角色为单手剑、双手剑、长柄武器角色时，角色普通攻击造成的伤害提高35%。"
//				}]
//    private Collects set;

  }

  /**
   * 命座
   */
  @Data
  public static class Constellation implements Serializable {
    
    private static final long serialVersionUID = 1L;

    private Long id;
    // 命座名称
    private String name;
    // 图片
    private String icon;
    // 描述/说明
    private String effect;
    // 是否激活
    private boolean is_actived;
    // 命座位置
    private Integer pos;

  }

}
