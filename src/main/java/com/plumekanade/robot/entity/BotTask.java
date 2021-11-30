package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author kanade
 * @date 2021-11-07 13:26
 */
@Data
@NoArgsConstructor
@TableName("bot_task")
public class BotTask implements Serializable {
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  // 任务简称
  private String name;
  // 提醒内容
  private String msg;
  // 开始时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date startTime;
  // 过期时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private LocalDateTime expireTime;
  // 任务状态 0已结束/过期 1正常
  private String state;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

  public BotTask(String name, String msg, Date startTime, LocalDateTime expireTime, String state) {
    this.name = name;
    this.msg = msg;
    this.expireTime = expireTime;
    this.state = state;
    this.startTime = startTime;
  }
}
