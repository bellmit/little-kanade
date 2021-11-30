package com.plumekanade.robot.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.plumekanade.robot.constants.DateConst;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 原神角色
 *
 * @author kanade
 * @version 1.0
 * @date 2021-11-19 10:51:46
 */
@Data
@TableName("genshin_avatar")
public class GenshinAvatar implements Serializable {
  private static final long serialVersionUID = 1L;

  @TableId
  private Long id;
  // 名称
  private String name;
  // 角色头像图片
  private String image;
  // 元素 参考元素枚举类
  private String element;
  // 星级
  private Integer rarity;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

}
