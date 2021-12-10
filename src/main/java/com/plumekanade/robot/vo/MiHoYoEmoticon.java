package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 米游社表情VO
 *
 * @author kanade
 * @version 1.0
 * @date 2021-05-05 15:22:47
 */
@Data
public class MiHoYoEmoticon implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;

  private Long id;
  private String name;
  private String icon;
  @JsonProperty(value = "sort_order")
  private int sort_order;
  @JsonProperty(value = "static_icon")
  private String static_icon;
  private Long updated_at;
  @JsonProperty(value = "is_available")
  private boolean is_available;
  private String status;
  private List<MiHoYoEmoticon> list;
  private int num;

}
