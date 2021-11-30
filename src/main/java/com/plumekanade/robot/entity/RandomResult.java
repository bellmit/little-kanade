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
 * 随机语句
 *
 * @author kanade
 * @version 1.0
 * @date 2021-04-26 16:41:58
 */
@Data
@TableName("random_result")
public class RandomResult implements Serializable {
  private static final long serialVersionUID = -4396704176951898689L;

  @TableId(type = IdType.AUTO)
  private Long id;
  // 内容
  private String content;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

}
