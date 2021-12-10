package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 群聊图片
 *
 * @author kanade
 * @version 1.0
 * @date 2021-08-24 14:11
 */
@Data
@NoArgsConstructor
@TableName("chat_image")
public class ChatImage implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;
  @TableId
  private Long id;
  // 图片名称
  private String filename;
  // 是否高清图 0否 1是
  private int hqState;
  // 图片路径
  private String path;
  //
  private String url;
  // 图片处理后的哈希值
  private String hash;
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

  public ChatImage(String filename, int hqState, String path, String hash, String url) {
    this.filename = filename;
    this.hqState = hqState;
    this.path = path;
    this.hash = hash;
    this.url = url;
  }
}
