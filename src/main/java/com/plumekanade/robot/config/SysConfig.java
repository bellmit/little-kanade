package com.plumekanade.robot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 项目配置类
 *
 * @author kanade
 * @date 2021-10-29 16:06
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sys-config")
public class SysConfig {

  private Long qq;
  private String wx;
  private String mhyCookie;
  private String robotName;
  private Long testGroup;
  private Long specialGroup;
  private String domain;
  private String wxAppId;
  private String wxAppSecret;
  private String wxAppToken;
  private String wxAesKey;
  private String wxOriginId;
  private List<String> nudges;
  // 腾讯云的API密钥id
  private String secretId;
  // 腾讯云的API密钥
  private String secretKey;
  // 腾讯云的智能对话平台botId
  private String botId;
  // 配置文件中的bot账号密码列表 下标: 偶数账号 奇数密码
  private List<String> bots;

}
