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
 * @author kanade
 * @date 2021-12-04 1:09
 */
@Data
@TableName("bot_function_word")
public class BotFunctionWord implements Serializable {
  private static final long serialVersionUID = 1L;

  @TableId(type = IdType.AUTO)
  private Long id;
  // 语句类型 0戳一戳回复 1早上打招呼 2中午打招呼 3下午打招呼 4傍晚打招呼 5晚上打招呼 6取消生气状态的回复 7被@或回复的响应
  private Integer type;
  // 语句
  private String word;
  // 创建时间
  @JsonFormat(pattern = DateConst.DT, timezone = DateConst.TZ)
  private Date createTime;

}
