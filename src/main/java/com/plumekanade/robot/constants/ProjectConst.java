package com.plumekanade.robot.constants;

import com.plumekanade.robot.entity.GenshinAvatar;

import java.util.Map;

/**
 * 项目常量
 *
 * @author kanade
 * @version 1.0
 * @date 2021-04-06 18:20:24
 */
public class ProjectConst {
  // 原神官服服务器编码 天空岛
  public static final String OFFICIAL_SERVER = "cn_gf01";
  // 原神B服服务器编码 世界树
  public static final String B_SERVER = "cn_qd01";
  // 原神id -> 角色
  public static Map<Long, GenshinAvatar> GENSHIN_AVATAR_MAP;
  // 系统配置
  public static Map<String, String> CONFIG_MAP;
  // 图库URL
  public static String GALLERY_URL;

  public static final Integer SUCCESS = 200;
  public static final int MULTIPLE = 1024;

  // 微信openid
//  public static final String OPEN_ID = "odUcN6LjO32BFa-5GCUqyXbW3WT4";
  // 分割线
  public static final String DIVIDER = "--------------------";
  // 换行
  public static final String WRAP = "\n";

  public static final String OK = "OK";
  public static final String POINT = ".";
  // 空格
  public static final String SPACE = " ";
  public static final String ONE = "1";
  public static final String ZERO = "0";
  public static final String SLASH = "/";
  public static final String VERTICAL = "\\|";
  public static final String FORBID_WORD = "立华奏橘奏立華 かなでたちばな かなでTachibalaKanade天使的心跳AngelBeatsangelbeatsab";
  public static final String PNG = "png";
  public static final String JPG = "jpg";
  public static final String REPLACE_CHAR = "X";

  // 图库普通图
  public static final String NORMAL_GALLERY_PATH = "/home/static/gallery/normal/";
  // 图库涩图
  public static final String SEXY_GALLERY_PATH = "/home/static/gallery/sexy/";
  // 图库露点图
  public static final String BARE_GALLERY_PATH = "/home/static/gallery/bare/";
  // 原图路径
  public static final String ORIGIN_PATH = "/home/static/gallery/origin/";

}
