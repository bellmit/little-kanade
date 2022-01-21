package com.plumekanade.robot.config;

import com.plumekanade.robot.constants.*;
import com.plumekanade.robot.entity.GenshinAvatar;
import com.plumekanade.robot.entity.SystemConfig;
import com.plumekanade.robot.handler.BotEventHandler;
import com.plumekanade.robot.service.GenshinAvatarService;
import com.plumekanade.robot.service.SystemConfigService;
import com.plumekanade.robot.utils.MiHoYoUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
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

    // 从数据库读取配置信息
    List<SystemConfig> configs = systemConfigService.list();
    setConfig(configs);  // 配置写入

    // 第一个为主 第二个LiYue辅助使用
    String[] auth = ProjectConst.CONFIG_MAP.get(SysKeyConst.BOT_AUTH).split(CmdConst.SEPARATOR2);

//    botLogin(Long.parseLong(auth1[0]), auth1[1], "小奏", "D:\\little-kanade\\cache\\minor");
//    return botLogin(Long.parseLong(auth[0]), auth[1], BotConst.NAME, null);
    return botLogin(Long.parseLong(auth[0]), auth[1], BotConst.NAME, "./minor");
  }

//  @Bean
//  public Bot kanadeBot() {
//    String[] auth = systemConfigService.getVal(SysKeyConst.BOT_AUTH1).split(CmdConst.SEPARATOR2);
//    return botLogin(Long.parseLong(auth[0]), auth[1], "小奏", "./minor");
//  }

  /**
   * 机器人登录流程
   */
  private Bot botLogin(Long qq, String pwd, String botName, String workDir) {
    Bot bot = BotFactory.INSTANCE.newBot(qq, pwd, new BotConfiguration() {
      {
        // 移除bot日志以及网络日志
        noBotLog();
        noNetworkLog();
        // 指定设备信息
        fileBasedDeviceInfo();
        if (StringUtils.isNotBlank(workDir)) {
          setWorkingDir(new File(workDir));
        }
//        fileBasedDeviceInfo("device.json");
//        setProtocol(MiraiProtocol.ANDROID_WATCH);
      }
    });
    try {
      bot.login();
      bot.getEventChannel().registerListenerHost(botEventHandler);
//      log.info("\n" + botName + "登录完成！");
      log.info("""

          ----------------------------
          Guard skill, Distortion!
          Guard skill, Hand Sonic!
          Guard skill, Wing!
          Guard skill, Overdrive!
          kanade has finished armed!
          ----------------------------""");
    } catch (Exception e) {
      log.error("【机器人登录】机器人登录出现异常, 账密: {} - {}, 堆栈信息: ", qq, pwd, e);
    }
    return bot;
  }

  /**
   * 配置写入
   */
  public static void setConfig(List<SystemConfig> configs) {

    ProjectConst.CONFIG_MAP = new HashMap<>(configs.size());
    configs.forEach(config -> ProjectConst.CONFIG_MAP.put(config.getParam(), config.getVal()));

    BotConst.NAME = ProjectConst.CONFIG_MAP.get(SysKeyConst.BOT_NAME);
    MiHoYoUtils.COOKIE = ProjectConst.CONFIG_MAP.get(SysKeyConst.MHY_COOKIE);
    BotConst.QQ = Long.parseLong(ProjectConst.CONFIG_MAP.get(SysKeyConst.QQ));
    ProjectConst.GALLERY_URL = ProjectConst.CONFIG_MAP.get(SysKeyConst.GALLERY_URL);
    BotConst.REPEAT_MODE = ProjectConst.ONE.equals(ProjectConst.CONFIG_MAP.get(SysKeyConst.REPEAT_MODE));
    BotConst.CANCEL_ANGRY = new ArrayList<>(Arrays.asList(ProjectConst.CONFIG_MAP.get(SysKeyConst.CANCEL_ANGRY).split(COMMA)));
    BotConst.AWAKE_KEYWORD = new ArrayList<>(Arrays.asList(ProjectConst.CONFIG_MAP.get(SysKeyConst.AWAKE_KEYWORD).split(COMMA)));
    APIConst.MO_LI_KEY = ProjectConst.CONFIG_MAP.get(SysKeyConst.MO_LI_KEY);
    APIConst.MO_LI_SECRET = ProjectConst.CONFIG_MAP.get(SysKeyConst.MO_LI_SECRET);
  }

}
