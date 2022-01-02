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
 * Q号关联Cookie
 *
 * @author kanade
 * @version 1.0
 * @date 2021-05-17 16:37:39
 */
@Data
@NoArgsConstructor
@TableName("cookie_lib")
public class CookieLib implements Serializable {

  @Serial
  private static final long serialVersionUID = 1L;
  // 游戏角色id
  @TableId(type = IdType.AUTO)
  private String id;
  private Long qq;
  // 原神游戏角色id
  private String ysId;
  // 米哈游账号cookie
  private String mhyCookie;
  // 微博自动签到cookie
  private String weiboCookie;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date updateTime;
  // 状态 0停用 1正常
  private Integer state;

  public CookieLib(Long qq, String ysId, String cookie) {
    this.ysId = ysId;
    this.qq = qq;
    this.mhyCookie = cookie;
  }

  public CookieLib(Long qq, String cookie) {
    this.qq = qq;
    this.weiboCookie = cookie;
  }

}
