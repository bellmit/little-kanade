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
 * 塔罗牌
 *
 * @author kanade
 * @date 2021-11-28 0:59
 */
@Data
@TableName("tarot")
public class Tarot implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  /**
   * 名称
   */
  private String tarotName;
  /**
   * 图片路径
   */
  private String path;
  /**
   * 含义
   */
  private String meaning;
  /**
   * 是否逆位(true) 顺位(false)
   */
  private Boolean reverse;
  /**
   * 创建时间
   */
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

}
