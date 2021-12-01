package com.plumekanade.robot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plumekanade.robot.entity.BotChat;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version 1.0
 * @author kanade
 * @date 2021-11-30 16:11
 */
@Repository
public interface BotChatMapper extends BaseMapper<BotChat> {

  /**
   * 获取回复被戳一戳的语句列表
   */
  @Select("select reply from bot_chat where dic_type = 0")
  List<String> getNudges();

}
