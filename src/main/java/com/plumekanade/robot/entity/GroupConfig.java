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
 * 群组配置
 *
 * @author kanade
 * @version 1.0
 * @date 2021-08-24 16:32
 */
@Data
@TableName("group_config")
public class GroupConfig implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  @TableId
  private Long id;
  // 群组管理员
  private String groupAdmin;
  // 群号
  private String groupCode;
  // 图片模式 0普通图 1涩图及以下 2露点及以下
  private int sexyState;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

}
