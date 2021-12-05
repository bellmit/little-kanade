package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.BotDic;
import com.plumekanade.robot.mapper.BotDicMapper;
import com.plumekanade.robot.utils.CommonUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @version 1.0
 * @author kanade
 * @date 2021-11-30 16:04
 */
@Service
@AllArgsConstructor
public class BotDicService extends ServiceImpl<BotDicMapper, BotDic> {

  private final BotDicMapper botDicMapper;

  /**
   * 反向模糊查询对应类型 -> 获取对应语句列表 -> 随机返回一句
   */
  public String queryTypeToGetWord(String msg) {
    List<String> replyList = botDicMapper.queryTypeToGetWord(msg);
    if (replyList.size() <= 0) {
      return null;
    }
    return replyList.get(CommonUtils.RANDOM.nextInt(replyList.size()));
  }
}
