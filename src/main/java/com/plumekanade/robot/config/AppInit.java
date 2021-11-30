package com.plumekanade.robot.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.plumekanade.robot.constants.BotConst;
import com.plumekanade.robot.constants.CmdConst;
import com.plumekanade.robot.constants.SysKeyConst;
import com.plumekanade.robot.entity.SystemConfig;
import com.plumekanade.robot.handler.BotEventHandler;
import com.plumekanade.robot.service.SystemConfigService;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.List;

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
  private SystemConfigService systemConfigService;
  @Resource
  private BotEventHandler botEventHandler;

  @Override
  public void run(ApplicationArguments args) {
    BotConst.NAME = systemConfigService.getVal("botName");

    log.info("\nGuard skill, Distortion! Guard skill, Hand Sonic! Guard skill, Wing! Guard skill, Overdrive! kanade has finished armed!\n");
  }

  /**
   * 初始化bot
   */
  @Bean
  public Bot bot() {
    // 默认只取一个 后面有需要再改多个
//    List<SystemConfig> list = systemConfigService.getLikeValList(SysKeyConst.BOT_AUTH);
//    Bot bot = BotFactory.INSTANCE.newBot(Long.parseLong(sysConfig.getBots().get(0)), sysConfig.getBots().get(1), new BotConfiguration() {
//      {
//        // 保存设备信息到文件
//        fileBasedDeviceInfo("deviceInfo.json");
//        // 设置登录协议
////        setProtocol(MiraiProtocol.ANDROID_WATCH);
//      }
//    });
    String[] arr = systemConfigService.getVal(SysKeyConst.BOT_AUTH).split(CmdConst.SEPARATOR2);
    Bot bot = BotFactory.INSTANCE.newBot(Long.parseLong(arr[0]), arr[1]);
    try {
      bot.login();
      bot.getEventChannel().registerListenerHost(botEventHandler);
    } catch (Exception e) {
      log.error("【Bot登录】机器人登录出现异常, 账密: {} - {}, 堆栈信息: ", arr[0], arr[1], e);
    }
    return bot;
  }

  /**
   * Mybatis-Plus分页插件
   */
  @Bean
  public MybatisPlusInterceptor paginationInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
  }

}
