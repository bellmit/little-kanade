package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-04-27 09:19:54
 */
@Data
@NoArgsConstructor
@TableName("player")
public class Player implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId(type = IdType.AUTO)
  private Long id;
  private Long code;
  private String nickname;
  // 积分
  private Long integral;
  // 连续活跃天数
  private Long seriesSign;
  // 总活跃天数
  private Long totalSign;
  // 上次活跃时间
  private Long lastSignTime;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date updateTime;

  public Player(Long code, String nickname) {
    this.code = code;
    this.nickname = nickname;
    this.lastSignTime = this.integral = this.seriesSign = this.totalSign = 0L;

  }
}
