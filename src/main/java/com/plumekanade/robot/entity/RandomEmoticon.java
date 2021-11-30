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
 * 随机表情
 *
 * @author kanade
 * @version 1.0
 * @date 2021-05-05 12:03:21
 */
@Data
@NoArgsConstructor
@TableName("random_emoticon")
public class RandomEmoticon implements Serializable {
  private static final long serialVersionUID = 5118106498906565020L;

  @TableId
  private Long id;
  private String path;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

  public RandomEmoticon(String path) {
    this.path = path;
  }
}
