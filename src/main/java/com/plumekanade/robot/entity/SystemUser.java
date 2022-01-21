package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统用户
 *
 * @author kanade
 * @date 2021-11-05 10:04
 */
@Data
@TableName("system_user")
public class SystemUser implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  // 用户名
  private String username;
  // 密码
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;
  // 昵称
  private String name;
  // 头像url地址
  private String avatar;
  // 用户状态 0停用 1正常
  private Integer state;
  // 最后登录时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date lastLogin;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;
  // 更新时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date updateTime;

}
