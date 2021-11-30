package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 聊天记录
 *
 * @author kanade
 * @version 1.0
 * @date 2021-04-27 09:29:06
 */
@Data
@NoArgsConstructor
@TableName("chat_record")
public class ChatRecord implements Serializable {
  private static final long serialVersionUID = -9143454979048142875L;

  @TableId(type = IdType.AUTO)
  private Long id;
  private String groupCode;
  private String accountCode;
  private String content;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

  public ChatRecord(String groupCode, String accountCode, String content) {
    this.groupCode = groupCode;
    this.accountCode = accountCode;
    this.content = content;
  }
}
