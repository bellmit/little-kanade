package com.plumekanade.robot.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 米哈游请求返回数据VO
 *
 * @author kanade
 * @date 2021-05-16 22:48
 */
@Data
public class MiHoYoResult implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  // 0成功
  private int retcode;
  private String message;
  private Object data;

}
