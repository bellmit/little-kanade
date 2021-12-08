package com.plumekanade.robot.constants;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

/**
 *
 * @version 1.0
 * @author kanade
 * @date 2021-11-29 16:18
 */
public class DateConst {

  public static final String TZ = "GMT+8";
  public static final String DT = "yyyy-MM-dd HH:mm:ss";
  public static final String PIXIV_DT = "yyyy-MM-dd'T'HH:mm:ss'+00:00'";
  public static final String PIXIV_TZ = "GMT+0";
  public static final SimpleDateFormat SDF = new SimpleDateFormat(DT);
  public static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern(DT);
  public static final SimpleDateFormat SMALL_SDF = new SimpleDateFormat("yy/MM/dd");
  // 一周秒数
  public static final long WEEK_SECONDS = 604800L;
  // 一分钟秒数
  public static final long SIXTY = 60L;
  // 20分钟秒数
  public static final long TWENTY_MINUTES_S = 1200L;

}
