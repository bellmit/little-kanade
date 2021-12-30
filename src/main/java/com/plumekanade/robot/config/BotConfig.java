package com.plumekanade.robot.config;

import com.plumekanade.robot.constants.BotConst;
import com.plumekanade.robot.constants.CmdConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.constants.SysKeyConst;
import com.plumekanade.robot.entity.GenshinAvatar;
import com.plumekanade.robot.handler.BotEventHandler;
import com.plumekanade.robot.service.GenshinAvatarService;
import com.plumekanade.robot.service.SystemConfigService;
import com.plumekanade.robot.utils.MiHoYoUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

import static com.baomidou.mybatisplus.core.toolkit.StringPool.COMMA;


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
  private final GenshinAvatarService genshinAvatarService;

  /**
   * 初始化bot
   */
  @Bean
  public Bot bot() {
    // 初始化原神角色map
    List<GenshinAvatar> avatars = genshinAvatarService.list();
    ProjectConst.GENSHIN_AVATAR_MAP = new HashMap<>(avatars.size());
    avatars.forEach(avatar -> ProjectConst.GENSHIN_AVATAR_MAP.put(avatar.getId(), avatar));

    Map<String, String> mapVal = systemConfigService.getMapVal(null);
    setConfig(mapVal);  // 配置写入

    // 默认只取一个 后面有需要再改多个
    String[] arr = mapVal.get(SysKeyConst.BOT_AUTH).split(CmdConst.SEPARATOR2);
    Bot bot = BotFactory.INSTANCE.newBot(Long.parseLong(arr[0]), arr[1], new BotConfiguration() {
      {
        // 移除bot日志以及网络日志
        noBotLog();
        noNetworkLog();
        // 指定设备信息
        fileBasedDeviceInfo();
//        fileBasedDeviceInfo("device.json");
//        setProtocol(MiraiProtocol.ANDROID_WATCH);
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

  /**
   * 配置写入
   */
  public static void setConfig(Map<String, String> mapVal) {

    BotConst.NAME = mapVal.get(SysKeyConst.BOT_NAME);
    MiHoYoUtils.COOKIE = mapVal.get(SysKeyConst.MHY_COOKIE);
    BotConst.QQ = Long.parseLong(mapVal.get(SysKeyConst.QQ));
    ProjectConst.GALLERY_URL = mapVal.get(SysKeyConst.GALLERY_URL);
    BotConst.REPEAT_MODE = ProjectConst.ONE.equals(mapVal.get(SysKeyConst.REPEAT_MODE));
    BotConst.CANCEL_ANGRY = new ArrayList<>(Arrays.asList(mapVal.get(SysKeyConst.CANCEL_ANGRY).split(COMMA)));
    BotConst.AWAKE_KEYWORD = new ArrayList<>(Arrays.asList(mapVal.get(SysKeyConst.AWAKE_KEYWORD).split(COMMA)));
  }

}
