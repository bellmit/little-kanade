package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 系统设置
 *
 * @author kanade
 * @version 1.0
 * @date 2021-11-27 15:05:01
 */
@Data
@TableName("system_config")
public class SystemConfig implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  /**
   * 设置key
   */
  private String param;
  /**
   * 设置val
   */
  private String val;
  /**
   * 备注信息
   */
  private String remark;
  /**
   * 创建时间
   */
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;
  /**
   * 更新时间
   */
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date updateTime;

}
