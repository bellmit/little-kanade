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
 * 机器人聊天表
 *
 * @author kanade
 * @version 1.0
 * @date 2021-11-30 16:04
 */
@Data
@TableName("bot_chat")
public class BotChat implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  /**
   * 机器人词典表的词性
   */
  private Integer dicType;
  /**
   * 回复/响应的语句
   */
  private String reply;
  /**
   * 创建时间
   */
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

}
