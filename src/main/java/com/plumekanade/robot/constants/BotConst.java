package com.plumekanade.robot.constants;

import net.mamoe.mirai.Bot;

import java.util.List;

/**
 * 机器人常量
 * @version 1.0
 * @author kanade
 * @date 2021-11-30 10:34
 */
public class BotConst {

  // 机器人名称
  public static String NAME;
  // 主人QQ
  public static Long QQ;
  // 复读机模式
  public static boolean REPEAT_MODE = false;
  // 取消生气状态的标志词
  public static List<String> CANCEL_ANGRY;
  // 唤醒机器人回复的关键字
  public static List<String> AWAKE_KEYWORD;

  // 机器人对象
  public static Bot BOT;

  // 特殊戳一戳语句
  public static final String WILL_ANGRY = "再戳就要生气了！";
  public static final String IS_ANGRY = "生气了";

  // mirai码字符串
  public static final String AT = "[mirai:at:";
  public static final String AT_END = "]";

  // 部分静态图片地址
  public static final String IMG_GS = "/home/littleKanade/imgSource/gaoshi.png";

}
