package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.constants.BotConst;
import com.plumekanade.robot.entity.RandomEmoticon;
import com.plumekanade.robot.mapper.RandomEmoticonMapper;
import com.plumekanade.robot.utils.CommonUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-05-05 12:08:35
 */
@Service
public class RandomEmoticonService extends ServiceImpl<RandomEmoticonMapper, RandomEmoticon> {

  /**
   * 获取随机表情
   */
  public String getRandomImage() {
    List<RandomEmoticon> list = list();
    if (list != null && list.size() > 0) {
      return list.get(CommonUtils.RANDOM.nextInt(list.size())).getPath();
    }
    return "唔...好像哪里不对劲呢，一定不是" + BotConst.NAME + "的问题！";
  }
}
