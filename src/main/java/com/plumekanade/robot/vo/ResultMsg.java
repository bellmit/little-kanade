package com.plumekanade.robot.vo;

import com.plumekanade.robot.enums.CodeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 方法返回vo
 *
 * @author kanade
 * @version 1.0
 * @date 2021-05-10 11:22:24
 */
@Data
@NoArgsConstructor
public class ResultMsg implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  private int code;
  private String msg;
  private Object data;

  public static ResultMsg success(Object data) {
    return new ResultMsg(CodeEnum.SUCCESS, data);
  }

  public static ResultMsg success() {
    return new ResultMsg(CodeEnum.SUCCESS, null);
  }

  public static ResultMsg error(CodeEnum resultEnum) {
    return new ResultMsg(resultEnum, null);
  }

  public static ResultMsg error(int code, String msg) {
    return new ResultMsg(code, msg);
  }

  public static ResultMsg error(String msg) {
    return new ResultMsg(CodeEnum.ERROR.getCode(), msg);
  }

  public ResultMsg(CodeEnum resultEnum, Object data) {
    this.code = resultEnum.getCode();
    this.msg = resultEnum.getMsg();
    this.data = data;
  }

  public ResultMsg(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }
}
