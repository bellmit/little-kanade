package com.plumekanade.robot.vo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 伤害计算参数VO
 *
 * @author kanade
 * @version 1.0
 * @date 2021-09-01 10:03
 */
@Data
public class HarmReq implements Serializable {
  private static final long serialVersionUID = 1L;

  // 攻击
  private Integer attack;
  // 元素加成
  private BigDecimal element;
  // 爆率
  private BigDecimal critical;
  // 爆伤
  private BigDecimal criticalDamage;

}
