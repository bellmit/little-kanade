package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色信息VO
 *
 * @author kanade
 * @version 1.0
 * @date 2021-07-29 16:47
 */
@Data
@JsonPropertyOrder(alphabetic = true)
public class GenshinInfo implements Serializable {
  private static final long serialVersionUID = 1L;

  // 人物列表
  private List<GenshinCharacter> avatars;
  // 账号汇总信息
  private GenshinAccountInfo stats;
  // 城市探索 ?
  private List<Object> city_explorations;
  // 世界探索
  private List<GenshinWorldInfo> world_explorations;
  // 尘世壶
  private List<GenshinHome> homes;

}
