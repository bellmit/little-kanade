package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 机器人词典/字典表
 *
 * @author kanade
 * @version 1.0
 * @date 2021-11-30 15:58
 */
@Data
@TableName("bot_dic")
public class BotDic implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId(type = IdType.AUTO)
  private Long id;
  /**
   * 词性 0陈述 1疑问 2反问 3禁止 4允许 5批评 6赞扬 7附和 8沮丧 9开心 10生气
   */
  private Integer type;
  /**
   * 字/词/句
   */
  private String word;
  /**
   * 创建时间
   */
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

}
