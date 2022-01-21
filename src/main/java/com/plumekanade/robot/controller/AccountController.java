package com.plumekanade.robot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plumekanade.robot.entity.AccountData;
import com.plumekanade.robot.entity.DelLog;
import com.plumekanade.robot.service.AccountDataService;
import com.plumekanade.robot.service.DelLogService;
import com.plumekanade.robot.utils.CommonUtils;
import com.plumekanade.robot.utils.MapperUtils;
import com.plumekanade.robot.vo.ResultMsg;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 *
 * @version 1.0
 * @author kanade
 * @date 2022-01-21 14:36
 */
@RestController
@AllArgsConstructor
@RequestMapping("/account")
public class AccountController {

  private final DelLogService delLogService;
  private final AccountDataService accountDataService;

  /**
   * 分页列表
   */
  @GetMapping("/getPage")
  public ResultMsg getPage(@RequestParam("page") int page, @RequestParam("size") int size,
                           @RequestParam(value = "target", required = false) String target) {
    LambdaQueryWrapper<AccountData> wrapper = new LambdaQueryWrapper<>();
    wrapper.orderByDesc(AccountData::getCreateTime);
    if (StringUtils.isNotBlank(target)) {
      wrapper.like(AccountData::getTarget, target);
    }
    Page<AccountData> result = accountDataService.page(new Page<>(page, size), wrapper);
    result.getRecords().forEach(accountData -> {
      String[] decryptArr = CommonUtils.decrypt(accountData.getCipherText(), accountData.getCount()).split(CommonUtils.SECRET);
      accountData.setUsername(decryptArr[0]);
      accountData.setCipherText(decryptArr[1]);
    });
    return ResultMsg.success(result);
  }

  /**
   * 添加/更新
   */
  @PostMapping("/dataSave")
  public ResultMsg dataSave(@RequestBody AccountData accountData) {
    String cipher = accountData.getUsername() + CommonUtils.SECRET + accountData.getCipherText();
    if (null == accountData.getId()) {
      accountData.setCipherText(CommonUtils.encrypt(cipher, accountData.getCount()));
      accountDataService.save(accountData);
    } else {
      AccountData dbData = accountDataService.getById(accountData.getId());
      // 解密获取原文
      String decrypt = CommonUtils.decrypt(dbData.getCipherText(), dbData.getCount());
      String[] dataArr = decrypt.split(CommonUtils.SECRET);

      if (!dataArr[1].equals(accountData.getCipherText())) {
        accountData.setCipherText(CommonUtils.encrypt(cipher, accountData.getCount()));
      }
      accountDataService.updateById(accountData);
    }
    return ResultMsg.success();
  }

  /**
   * 删除
   */
  @PostMapping("/delete")
  public ResultMsg delete(@RequestBody AccountData accountData) {
    accountData = accountDataService.getById(accountData.getId());
    delLogService.save(new DelLog("AccountData", MapperUtils.serialize(accountData)));
    return ResultMsg.success("" + accountDataService.removeById(accountData.getId()));
  }

}
