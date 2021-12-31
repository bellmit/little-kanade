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
  // 图库URL
  public static String GALLERY_URL;

  public static final Integer SUCCESS = 200;
  public static final int MULTIPLE = 1024;

  // 微信openid
//  public static final String OPEN_ID = "odUcN6LjO32BFa-5GCUqyXbW3WT4";
  // 分割线
  public static final String DIVIDER = "--------------------";
  // 目标
  public static final String TARGET = "数据目标: ";
  // 用户名
  public static final String USERNAME = "un: ";
  // 密码
  public static final String PASSWORD = "pwd: ";
  // 换行
  public static final String WRAP = "\n";

  // 茉莉灵签参数
  public static final String JOKE = "笑话";
  public static final String GUAN_YIN_LOT = "观音灵签";
  public static final String YUE_LAO_LOT = "月老灵签";
  public static final String CAI_SHEN_LOT = "财神爷灵签";

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

  // 请求类型
  public static final String CONTENT_TYPE = "Content-Type";
  public static final String CONTENT_TYPE_JSON = "application/json";

  // 图库普通图
  public static final String NORMAL_GALLERY_PATH = "/home/static/gallery/normal";
  // 图库涩图
  public static final String SEXY_GALLERY_PATH = "/home/static/gallery/sexy";
  // 图库露点图
  public static final String BARE_GALLERY_PATH = "/home/static/gallery/bare";
}
