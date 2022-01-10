package com.plumekanade.robot.constants;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-04-17 11:21:01
 */
public class APIConst {

  public static final String CONTENT_TYPE_X_WWW = "application/x-www-form-urlencoded; charset=utf-8";

  // 茉莉机器人 www.itpk.cn
  public static final String MO_LI_KEY = "8994ea991ed9e235cc42eded32181edd";
  public static final String MO_LI_SECRET = "plumekanade";
  public static final String MO_LI_API = "http://i.itpk.cn/api.php?limit=8&api_key=" +
      MO_LI_KEY + "&api_secret=" + MO_LI_SECRET + "&question=";

  // 图片索引API post 或 get 参数详见LoLiCon
  public static final String LO_LI_CON_IMG = "https://api.lolicon.app/setu/v2";
  // pixiv的图片反代地址
  public static final String IMG_PROXY_DOMAIN = "i.acgmx.com";

  // 微博签到API
  public static final String WEI_BO_SIGN = "https://weibo.com/p/aj/general/button?ajwvr=6&api=http://i.huati.weibo.com/aj/super/checkin&texta=%E7%AD%BE%E5%88%B0&textb=%E5%B7%B2%E7%AD%BE%E5%88%B0&status=0&id=100808fc439dedbb06ca5fd858848e521b8716&location=page_100808_super_index&timezone=GMT+0800&lang=zh-cn&plat=Win32&ua=Mozilla/5.0%20(Windows%20NT%206.1;%20Win64;%20x64)%20AppleWebKit/537.36%20(KHTML,%20like%20Gecko)%20Chrome/96.0.4664.110%20Safari/537.36%20Edg/96.0.1054.62&screen=1920*1080&__rnd=1641779620699&sudaref=weibo.com&display=0&retcode=6102";
  // 请求头Referer
  public static final String WEI_BO_REFERER = "https://weibo.com/p/100808fc439dedbb06ca5fd858848e521b8716/super_index";
  // 微博领奖励API


  // 腾讯自然语言处理(NLP)API 50w次/天 文档地址: https://cloud.tencent.com/document/product/271/35486
//  public static final String NLP_DOMAIN = "https://nlp.tencentcloudapi.com";
//  public static final String NLP_REGION = "ap-guangzhou";


  //===================================== 米哈游 原神API =====================================//
  public static final String MI_HO_YO_SERVER = "https://api-takumi.mihoyo.com";
  // 账号角色
  public static final String GEN_SHIN_ROLE = MI_HO_YO_SERVER + "/binding/api/getUserGameRolesByCookie?game_biz=hk4e_cn";
  // 签到
  public static final String GEN_SHIN_SIGN = MI_HO_YO_SERVER + "/event/bbs_sign_reward/sign";
  // 用户信息
//  public static final String GEN_SHIN_INFO = MI_HO_YO_SERVER + "/game_record/genshin/api/index?server=cn_gf01&role_id=";
  public static final String GEN_SHIN_INFO = MI_HO_YO_SERVER + "/game_record/app/genshin/api/index?server=CHANNEL&role_id=";
  // 螺旋深渊（本期？）
  public static final String GEN_SHIN_ABYSS_THIS = MI_HO_YO_SERVER + "/game_record/app/genshin/api/spiralAbyss?schedule_type=1&server=cn_gf01&role_id=";
  // 螺旋深渊（上期？）
  public static final String GEN_SHIN_ABYSS_PREVIOUS = MI_HO_YO_SERVER + "/game_record/app/genshin/api/spiralAbyss?schedule_type=2&server=cn_gf01&role_id=";
  // 人物信息 POST
  public static final String GEN_SHIN_CHARACTER_INFO = MI_HO_YO_SERVER + "/game_record/app/genshin/api/character";
  // 米游社表情API
  public static final String EMOTICON = "https://api-static.mihoyo.com/takumi/misc/api/emoticon_set?gids=2";
  //===================================== 米哈游 原神API =====================================//

}
