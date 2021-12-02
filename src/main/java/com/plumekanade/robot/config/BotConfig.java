package com.plumekanade.robot.config;

import com.plumekanade.robot.constants.BotConst;
import com.plumekanade.robot.constants.CmdConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.constants.SysKeyConst;
import com.plumekanade.robot.handler.BotEventHandler;
import com.plumekanade.robot.service.SystemConfigService;
import com.plumekanade.robot.utils.MiHoYoUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * @author kanade
 * @date 2021-12-02 23:03
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class BotConfig {

  private final BotEventHandler botEventHandler;
  private final SystemConfigService systemConfigService;

  /**
   * 初始化bot
   */
  @Bean
  public Bot bot() {
    Map<String, String> mapVal = systemConfigService.getMapVal();
    // 配置写入
    BotConst.QQ = Long.parseLong(mapVal.get(SysKeyConst.QQ));
    BotConst.NAME = mapVal.get(SysKeyConst.BOT_NAME);
    MiHoYoUtils.COOKIE = mapVal.get(SysKeyConst.MHY_COOKIE);
    BotConst.REPEAT_MODE = Boolean.parseBoolean(mapVal.get(SysKeyConst.REPEAT_MODE));
    BotConst.CANCEL_ANGRY = new ArrayList<>(Arrays.asList(mapVal.get(SysKeyConst.CANCEL_ANGRY).split(ProjectConst.COMMA)));
    BotConst.AWAKE_KEYWORD = new ArrayList<>(Arrays.asList(mapVal.get(SysKeyConst.AWAKE_KEYWORD).split(ProjectConst.COMMA)));

    // 默认只取一个 后面有需要再改多个
    String[] arr = mapVal.get(SysKeyConst.BOT_AUTH).split(CmdConst.SEPARATOR2);
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
      BotConst.BOT = bot;
      log.info("""
          
          ----------------------------
          Guard skill, Distortion!
          Guard skill, Hand Sonic!
          Guard skill, Wing!
          Guard skill, Overdrive!
          kanade has finished armed!
          ----------------------------""");
    } catch (Exception e) {
      log.error("【机器人登录】机器人登录出现异常, 账密: {} - {}, 堆栈信息: ", arr[0], arr[1], e);
    }
    return bot;
  }

}
