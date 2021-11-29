package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 原神角色信息请求VO
 *
 * @author kanade
 * @version 1.0
 * @date 2021-11-18 10:51:47
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonPropertyOrder(alphabetic = true)
public class GenshinCharacterReq implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty(value = "character_ids")
  private List<Long> characterIds;
  @JsonProperty(value = "role_id")
  private String roleId;
  private String server;

}
