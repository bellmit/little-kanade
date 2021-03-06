package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
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
 * @date 2021-10-30 2:23
 */
@Data
@NoArgsConstructor
@TableName("account_data")
public class AccountData implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  // 账密的使用地点/网站
  private String target;
  // 未加密前的账号
  @TableField(exist = false)
  private String username;
  // 加密后的数据/未加密前的密码
  private String cipherText;
  // 次数(加密)
  private Integer count;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

  public AccountData(String target, String cipherText, Integer count) {
    this.target = target;
    this.cipherText = cipherText;
    this.count = count;
  }
}
