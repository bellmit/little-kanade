package com.plumekanade.robot.entity;

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
 * acg相关进度记录
 *
 * @author kanade
 * @date 2021-10-22 16:48
 */
@Data
@NoArgsConstructor
@TableName("acg_record")
public class AcgRecord implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  // 漫画/动画/小说名称
  private String acgName;
  // 观看进度 如第几话/集/章
  private String progress;
  // 标签 多项逗号隔开
  private String tag;
  // 类型 0动画 1漫画 2小说
  private int type;
  // 状态 0在看(追) 1看(追)完 2弃
  private int state;
  // 所属季度 如: 202107、202010
  private String quarter;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;
  // 更新时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date updateTime;

  public AcgRecord(String acgName, String progress, String tag, int type, int state, String quarter) {
    this.acgName = acgName;
    this.progress = progress;
    this.tag = tag;
    this.type = type;
    this.state = state;
    this.quarter = quarter == null ? "" : quarter;
  }
}
