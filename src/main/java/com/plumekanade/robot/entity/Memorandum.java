package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 备忘录
 * @author kanade
 * @date 2021-11-14 22:44
 */
@Data
@NoArgsConstructor
@TableName("memorandum")
public class Memorandum implements Serializable {
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  // 标题
  private String title;
  // 内容
  private String content;
  // 是否需要公众号提醒
  private boolean remindFlg;
  // 提醒时间
  private Date remindTime;
  // 状态 0已结束/不需要提醒 1待提醒
  private Integer state;
  // 创建时间
  private Date createTime;

  public Memorandum(String title, String content, Date remindTime) {
    this.title = title;
    this.content = content;
    if (null != remindTime) {
      this.remindTime = remindTime;
      this.remindFlg = true;
      this.state = 1;
    }
  }

}
