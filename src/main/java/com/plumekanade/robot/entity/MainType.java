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
 * 项目主要的通用类型
 *
 * @author kanade
 * @version 1.0
 * @date 2021-04-27 09:01:42
 */
@Data
@TableName("main_type")
public class MainType implements Serializable {
  private static final long serialVersionUID = 5780972207696549190L;

  @TableId(type = IdType.AUTO)
  private Long id;
  private String name;
  private String code;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

}
