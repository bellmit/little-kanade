package com.plumekanade.robot.enums;

import lombok.Getter;

/**
 * 返回值枚举类
 * @version 1.0
 * @author kanade
 * @date 2021-11-29 16:46
 */
@Getter
public enum CodeEnum {

  SUCCESS(0, "ok"),
  ERROR(-1, "服务器出了点差错"),

  UN_LOGIN(600, "用户未登录"),
  PWD_ERROR(601, "账号或密码有误")

  ;

  private final int code;
  private final String msg;

  CodeEnum(int code, String msg) {
    this.code = code;
    this.msg = msg;
  }

}
