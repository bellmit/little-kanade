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
 * 丘丘语翻译
 *
 * @author kanade
 * @version 1.0
 * @date 2021-11-23 17:30:01
 */
@Data
@TableName("qiu_qiu_translate")
public class QiuQiuTranslate implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  @TableId(type = IdType.AUTO)
  private Long id;
  // 丘丘语
  private String qiuQiuLanguage;
  // 译文
  private String translation;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

}
