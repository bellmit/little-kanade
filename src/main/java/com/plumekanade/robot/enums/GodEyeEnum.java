package com.plumekanade.robot.enums;

import lombok.Getter;

/**
 * 神之眼属性枚举
 *
 * @author kanade
 * @version 1.0
 * @date 2021-07-29 16:13
 */
@Getter
public enum GodEyeEnum {

  Electro("Electro", "雷"),
  Pyro("Pyro", "火"),
  Geo("Geo", "岩"),
  Cryo("Cryo", "冰"),
  Anemo("Anemo", "风"),
  Hydro("Hydro", "水"),

  ;

  private final String name;
  private final String value;

  GodEyeEnum(String name, String value) {
    this.name = name;
    this.value = value;
  }

}
