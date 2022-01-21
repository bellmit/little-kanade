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
 * 数据记录的删除操作日志
 * @version 1.0
 * @author kanade
 * @date 2022-01-21 11:49
 */
@Data
@TableName("del_log")
@NoArgsConstructor
public class DelLog implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  // 实体类名称
  private String entityName;
  // 删除的记录内容
  private String content;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

  public DelLog(String entityName, String content) {
    this.entityName = entityName;
    this.content = content;
  }
}
