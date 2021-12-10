package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 原神角色VO
 *
 * @author kanade
 * @date 2021-05-16 23:09
 */
@Data
@JsonPropertyOrder(alphabetic = true)
public class GenShinRole implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  // ?
  @JsonProperty("game_biz")
  private String game_biz;
  // 服务器_渠道(cn_gf01 - 国服、cn_qd01 - B服)
  private String region;
  // 游戏uid
  @JsonProperty("game_uid")
  private String game_uid;
  // 游戏名称
  private String nickname;
  // 游戏等级
  private int level;
  // ?
  @JsonProperty("is_chosen")
  private boolean is_chosen;
  // 服务器名称
  @JsonProperty("region_name")
  private String region_name;
  // ?
  @JsonProperty("is_official")
  private boolean is_official;

}
