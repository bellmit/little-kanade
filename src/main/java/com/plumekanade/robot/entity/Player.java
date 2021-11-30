package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-04-27 09:19:54
 */
@Data
@TableName("player")
public class Player implements Serializable {
  private static final long serialVersionUID = 5275725724924543202L;

  @TableId(type = IdType.AUTO)
  private Long id;
  private String code;
  private String nickname;
  // 米忽悠的账号
  private String miHoYoAccount;
  // 米忽悠的密码
  private String miHoYoPassword;
  // 米忽悠的cookie
  private String miHoYoCookie;
  private int integral;
  private int seriesSign;
  private int totalSign;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date updateTime;

}
