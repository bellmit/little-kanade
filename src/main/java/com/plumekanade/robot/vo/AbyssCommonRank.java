package com.plumekanade.robot.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 原神深境螺旋通用数据统计的角色VO
 *
 * @author kanade
 * @version 1.0
 * @date 2021-11-19 11:05:58
 */
@Data
public class AbyssCommonRank implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  // 角色id
  private Long avatar_id;
  // 角色头像图片
  private String avatar_icon;
  // 出战次数
  private Integer value;
  // 角色星级
  private Integer rarity;

  public AbyssCommonRank() {
    this.value = 0;
    this.avatar_id = null;
  }
}
