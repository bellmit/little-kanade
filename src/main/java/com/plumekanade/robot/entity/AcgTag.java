package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * ACG标签
 *
 * @author kanade
 * @date 2021-11-04 14:19
 */
@Data
@NoArgsConstructor
@TableName("acg_tag")
public class AcgTag implements Serializable {
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  // 名称
  private String name;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

  public AcgTag(String name) {
    this.name = name;
  }
}
