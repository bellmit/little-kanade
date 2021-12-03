package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.entity.Memorandum;
import com.plumekanade.robot.mapper.MemorandumMapper;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author kanade
 * @date 2021-11-14 22:49
 */
@Service
public class MemorandumService extends ServiceImpl<MemorandumMapper, Memorandum> {

  /**
   * 根据备忘录标题添加/修改备忘录 模糊查询
   */
  public void saveOrUpdateByTitle(String title, String content, Date remindTime) {
    Memorandum memorandum = getOne(new LambdaQueryWrapper<Memorandum>().like(Memorandum::getTitle, title));
    if (null != memorandum) {
      memorandum.setContent(content);
      if (null != remindTime) {
        memorandum.setRemindTime(remindTime);
        memorandum.setRemindFlg(true);
      }
      updateById(memorandum);
      return;
    }
    save(new Memorandum(title, content, remindTime));
  }

}
