package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.entity.AccountData;
import com.plumekanade.robot.mapper.AccountDataMapper;
import com.plumekanade.robot.utils.CommonUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author kanade
 * @date 2021-10-30 2:29
 */
@Service
public class AccountDataService extends ServiceImpl<AccountDataMapper, AccountData> {

  /**
   * 通过微信修改敏感数据的密码
   *
   * @return 如果为null则是修改成功 否则返回多条数据
   */
  public String wxModifyPwd(String targetName, String pwd) {
    List<AccountData> list = list(new LambdaQueryWrapper<AccountData>().like(AccountData::getTarget, targetName));

    if (list.size() == 1) {
      AccountData accountData = list.get(0);
      String decrypt = CommonUtils.decrypt(accountData.getCipherText(), accountData.getCount());
      // 0用户名 1密码
      String[] split = decrypt.split(CommonUtils.SECRET);
      accountData.setCipherText(CommonUtils.encrypt(split[0] + CommonUtils.SECRET + pwd, accountData.getCount()));
      updateById(accountData);
      return null;

    } else {
      StringBuilder builder = new StringBuilder();
      builder.append("查询到多条匹配的数据, 请详写查询条件");
      for (AccountData accountData : list) {
        builder.append(accountData.getTarget()).append(ProjectConst.WRAP);
      }
      return builder.toString();
    }
  }

  /**
   * 添加敏感数据记录
   *
   * @param msgArr 0指令 1目标 2账号 3密码 4加密次数
   */
  public String addData(String[] msgArr) {
    AccountData accountData = getOne(new LambdaQueryWrapper<AccountData>().eq(AccountData::getTarget, msgArr[1]));
    if (null != accountData) {
      return "已有相关数据匹配, 可查询查看";
    }
    String rawText = msgArr[2] + CommonUtils.SECRET + msgArr[3];
    int count = Integer.parseInt(msgArr[4]);
    save(new AccountData(msgArr[1], CommonUtils.encrypt(rawText, count), count));
    return null;
  }
}
