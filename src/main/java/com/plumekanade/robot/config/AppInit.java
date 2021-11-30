package com.plumekanade.robot.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.plumekanade.robot.constants.BotConst;
import com.plumekanade.robot.constants.CmdConst;
import com.plumekanade.robot.constants.SysKeyConst;
import com.plumekanade.robot.handler.BotEventHandler;
import com.plumekanade.robot.service.SystemConfigService;
import com.plumekanade.robot.utils.MiHoYoUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 随启动执行
 *
 * @author kanade
 * @version 1.0
 * @date 2021-05-24 11:47:28
 */
@Slf4j
@Configuration
public class AppInit implements ApplicationRunner {

  @Resource
  private BotEventHandler botEventHandler;
  @Resource
  private SystemConfigService systemConfigService;

  @Override
  public void run(ApplicationArguments args) {
    BotConst.NAME = systemConfigService.getVal(SysKeyConst.BOT_NAME);
    BotConst.QQ = systemConfigService.getVal(SysKeyConst.QQ);
    BotConst.REPEAT_MODE = Boolean.parseBoolean(systemConfigService.getVal(SysKeyConst.REPEAT_MODE));
    MiHoYoUtils.COOKIE = systemConfigService.getVal(SysKeyConst.MHY_COOKIE);

    log.info("\nGuard skill, Distortion! Guard skill, Hand Sonic! Guard skill, Wing! Guard skill, Overdrive! kanade has finished armed!\n");
  }

  /**
   * 初始化bot
   */
  @Bean
  public Bot bot() {
    // 默认只取一个 后面有需要再改多个
//    List<SystemConfig> list = systemConfigService.getLikeValList(SysKeyConst.BOT_AUTH);

    String[] arr = systemConfigService.getVal(SysKeyConst.BOT_AUTH).split(CmdConst.SEPARATOR2);
    Bot bot = BotFactory.INSTANCE.newBot(Long.parseLong(arr[0]), arr[1], new BotConfiguration() {
      {
        // 移除bot日志以及网络日志
        noBotLog();
        noNetworkLog();
        // 指定设备信息
        fileBasedDeviceInfo();
      }
    });
    try {
      bot.login();
      bot.getEventChannel().registerListenerHost(botEventHandler);
      log.info("【机器人登录】小奏登录成功!");
    } catch (Exception e) {
      log.error("【机器人登录】机器人登录出现异常, 账密: {} - {}, 堆栈信息: ", arr[0], arr[1], e);
    }
    return bot;
  }

}
