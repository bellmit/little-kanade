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
 * 图库（指令触发）
 *
 * @author kanade
 * @version 1.0
 * @date 2021-08-24 14:22
 */
@Data
@NoArgsConstructor
@TableName("gallery")
public class Gallery implements Serializable {
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  // 图片原名称
  private String originName;
  // 图片现名（暂不改名）
  private String filename;
  // 图片模式 0普通 1涩 2露点
  private int sexyState;
  // 文件路径
  private String path;
  // 独立访问url
  private String url;
  // 图片处理后的哈希值
  private String hash;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

  public Gallery(String originName, String filename, int sexyState, String path, String url, String hash) {
    this.originName = originName;
    this.filename = filename;
    this.sexyState = sexyState;
    this.path = path;
    this.url = url;
    this.hash = hash;
  }
}
