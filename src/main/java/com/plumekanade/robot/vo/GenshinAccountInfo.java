package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 账号汇总信息
 *
 * @author kanade
 * @version 1.0
 * @date 2021-07-29 16:50
 */
@Data
@JsonPropertyOrder(alphabetic = true)
public class GenshinAccountInfo implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  // 登录天数
  private Integer active_day_number;
  // 成就数量
  private Integer achievement_number;
  // 胜率?没看到用来干嘛的
  private Integer win_rate;
  // 风神瞳数
  private Integer anemoculus_number;
  // 岩神瞳数
  private Integer geoculus_number;
  // 雷神瞳数
  private Integer electroculus_number;
  // 火神瞳数
  private Integer pyroculus_number;
  // 冰神瞳数
  private Integer cryoculus_number;
  // 水神瞳数
  private Integer hydroculus_number;
  // 草神瞳数
//  private Integer
  // 人物数量
  private Integer avatar_number;
  // 解锁传送点数
  private Integer way_point_number;
  // 解锁秘境数
  private Integer domain_number;
  // 深境螺旋
  private String spiral_abyss;
  // 华丽宝箱数
  private Integer luxurious_chest_number;
  // 珍贵宝箱数
  private Integer precious_chest_number;
  // 精致宝箱数
  private Integer exquisite_chest_number;
  // 普通宝箱数
  private Integer common_chest_number;


}
