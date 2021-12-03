package com.plumekanade.robot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plumekanade.robot.entity.BotFunctionWord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author kanade
 * @date 2021-12-04 1:11
 */
@Repository
public interface BotFunctionWordMapper extends BaseMapper<BotFunctionWord> {

  /**
   * 获取戳一戳回复的语句
   */
  @Select("select word from bot_function_word where type = #{type}")
  List<String> getWords(@Param("type") int type);

}
