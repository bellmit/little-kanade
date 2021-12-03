package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.BotFunctionWord;
import com.plumekanade.robot.mapper.BotFunctionWordMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kanade
 * @date 2021-12-04 1:12
 */
@Service
@AllArgsConstructor
public class BotFunctionWordService extends ServiceImpl<BotFunctionWordMapper, BotFunctionWord> {

  private final BotFunctionWordMapper botFunctionWordMapper;

  /**
   * 获取戳一戳的回复语句
   * 0戳一戳回复 1早上打招呼 2中午打招呼 3下午打招呼 4傍晚打招呼 5晚上打招呼
   */
  public List<String> getWords(int type) {
    return botFunctionWordMapper.getWords(type);
  }
}
