package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 人物信息
 *
 * @author kanade
 * @version 1.0
 * @date 2021-07-29 16:06
 */
@Data
@JsonPropertyOrder(alphabetic = true)
public class GenshinCharacter implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  // id
  private Long id;
  // 图片 当旅行者角色为 空 时 图片地址包含 UI_AvatarIcon_PlayerBoy
  // 当旅行者角色为 荧 时 图片地址包含 UI_AvatarIcon_PlayerGirl
  private String image;
  // 名称
  private String name;
  // 属性(神之眼)
  private String element;
  // 好感度
  private Integer fetter;
  // 等级
  private Integer level;
  // 星级
  private Integer rarity;
  // 命座数
  private Integer actived_constellation_num;


}
