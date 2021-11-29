package com.plumekanade.robot.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.plumekanade.robot.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

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
  private SysConfig sysConfig;

  @Override
  public void run(ApplicationArguments args) {
    log.info(MapperUtils.serialize(sysConfig));

    log.info("Guard skill, Distortion! Guard skill, Hand Sonic! Guard skill, Wing! Guard skill, Overdrive! kanade has finished armed!");
  }

  /**
   * 初始化bot
   */
  @Bean
  public Bot bot() {
    // 默认只取一个 后面有需要再改多个
    Bot bot = BotFactory.INSTANCE.newBot(Long.parseLong(sysConfig.getBots().get(0)), sysConfig.getBots().get(1));
    try {
      bot.login();
    } catch (Exception e) {
      log.error("【Bot登录】机器人登录出现异常, 账密: {} - {}, 堆栈信息: ", sysConfig.getBots().get(0), sysConfig.getBots().get(1), e);
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
