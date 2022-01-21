package com.plumekanade.robot.constants;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 登录拦截相关常量
 * @version 1.0
 * @author kanade
 * @date 2022-01-20 17:29
 */
public class AuthConst {

  public static final String TOKEN_HEADER = "kanade-token";
  public static final BCryptPasswordEncoder PWD_ENCODER = new BCryptPasswordEncoder();
  public static final List<String> EXCLUDE_PATH = new ArrayList<>(Arrays.asList("common", "wechat", "pixivParse"));

}
