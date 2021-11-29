package com.plumekanade.robot.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * TextProcessResponse -> ResponseMessage -> groupList ↓
 * @author kanade
 * @date 2021-11-28 17:45
 */
@Data
public class TextProcessResult implements Serializable {
  private static final long serialVersionUID = 1L;

  private String ContentType;
  private String url;
  private String content;

}
