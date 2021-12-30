package com.plumekanade.robot.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Base64;
import java.util.Random;

/**
 * @author kanade
 * @date 2021-10-29 15:53
 */
public class CommonUtils {

  // 敏感数据加密的密钥
  public static final String SECRET = "dGFueGluZ3hpYW4";
  // 随机串的字符库
  private static final String DICT_LETTER = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
  public static final Random RANDOM = new Random();

  /**
   * 原神随机字符串(不带数字)
   */
  public static String random(int length) {
    StringBuilder result = new StringBuilder();
    for (int i = 0; i < length; i++) {
      result.append(DICT_LETTER.charAt((int) (Math.random() * DICT_LETTER.length())));
    }
    return result.toString();
  }

  /**
   * 指定长度随机数字字符串
   *
   * @date 2021-08-24 11:42
   */
  public static String randomNum(int len) {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < len; i++) {
      builder.append(RANDOM.nextInt(9) + 1);
    }
    return builder.toString();
  }

  /**
   * 时间id
   *
   * @date 2021-08-24 11:39
   */
  public static String id() {
    return DTF.format(LocalDateTime.now()) + randomNum(5);
  }

  /**
   * 获取分页对象
   */
  public static <T> Page<T> getPage(int page, int size) {
    page = Math.max(page, 1);
    size = Math.max(size, 1);
    return new Page<>(page, size);
  }

  /**
   * 加密敏感数据 n次加密只要对应上n次解密即可
   *
   * @param data 未加密的敏感数据 account + SECRET + pwd
   */
  public static String encrypt(String data, int n) {
    if (n > 1) {
      data = encrypt(data, --n);
    }
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < data.length(); i++) {
      int ascii = data.charAt(i);
      // 偶数+4 奇数-4
      ascii = i % 2 == 0 ? ascii + 4 : ascii - 4;
      char encryptChar = (char) ascii;
      builder.append(encryptChar);
    }
    // base64加密
    return Base64.getEncoder().encodeToString(builder.toString().getBytes(StandardCharsets.UTF_8));
  }

  /**
   * 解密敏感数据 n次加密只要对应上n次解密即可
   *
   * @return 完全解密后返回明文格式为: account + SECRET + pwd
   */
  public static String decrypt(String data, int n) {
    if (n > 1) {
      data = decrypt(data, --n);
    }
    // base64解密
    data = new String(Base64.getDecoder().decode(data), StandardCharsets.UTF_8);
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < data.length(); i++) {
      int ascii = data.charAt(i);
      // 偶数-4 奇数+4
      ascii = i % 2 == 0 ? ascii - 4 : ascii + 4;
      char decryptChar = (char) ascii;
      builder.append(decryptChar);
    }
    return builder.toString();
  }

  /**
   * 获取距离深渊刷新的天数
   */
  public static int[] getSpiralAbyssSurplusDays() {
    int[] result = new int[]{0, 0};
    LocalDateTime ldt = LocalDateTime.now();
    int day = ldt.getDayOfMonth();
    int hour = ldt.getHour();
    if (day < 16) {
      result[0] = 15 - day;
      result[1] = 27 - hour;
    } else {
      if (day == 16 && hour <= 4) {
        result[1] = 4 - hour;
      } else {
        result[0] = ldt.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth() - day + 1;
        result[1] = 27 - hour;
      }
    }

    return result;
  }

  /**
   * 文件写入 InputStream写入到指定文件
   */
  public static File writeFile(InputStream is, String path) throws Exception {
    File file = new File(path);
    if (!file.exists()) {
      file.createNewFile();
    }
    FileOutputStream fos = new FileOutputStream(file);
    byte[] b = new byte[1024];
    while (is.read(b) != -1) {
      fos.write(b);// 写入数据
    }
    is.close();
    fos.close();
    return file;
  }

}
