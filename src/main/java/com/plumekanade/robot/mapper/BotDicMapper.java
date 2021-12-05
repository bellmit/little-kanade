package com.plumekanade.robot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plumekanade.robot.entity.BotDic;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @version 1.0
 * @author kanade
 * @date 2021-11-30 16:03
 */
@Repository
public interface BotDicMapper extends BaseMapper<BotDic> {

  /**
   * 反向模糊查询对应类型 -> 获取对应语句列表 -> 随机返回一句
   */
  @Select("select c.reply from bot_chat c, bot_dic d where d.type = c.dic_type and locate(d.word, #{msg})")
  List<String> queryTypeToGetWord(@Param("msg") String msg);
}
