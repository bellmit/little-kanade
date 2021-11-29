package com.plumekanade.robot.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis配置类
 *
 * @author kanade
 * @version 1.0
 * @date 2021-08-25 10:59
 */
@Data
@Slf4j
@Configuration
public class RedisConfig {

  @Value("${spring.redis.host}")
  private String host;
  @Value("${spring.redis.port}")
  private int port;
  @Value("${spring.redis.password}")
  private String password;
  // 连接超时时间（毫秒）
  @Value("${spring.redis.timeout}")
  private int timeout;
  // 连接池最大连接数（使用负值表示没有限制）
  @Value("${spring.redis.jedis.pool.max-active}")
  private int maxActive;
  // 连接池中的最大空闲连接
  @Value("${spring.redis.jedis.pool.max-idle}")
  private int maxIdle;
  // 连接池中的最小空闲连接
  @Value("${spring.redis.jedis.pool.min-idle}")
  private int minIdle;

  /**
   * @date 2021-08-26 11:50
   */
  @Bean(name = "redisZero")
  public JedisPool certPool() {
    return new JedisPool(getConfig(), host, port, timeout, password, 0);
  }

  /**
   * @date 2021-11-27 15:12
   */
  @Bean(name = "redisOne")
  public JedisPool chatPool() {
    return new JedisPool(getConfig(), host, port, timeout, password, 1);
  }

  /**
   * 获取配置
   */
  private JedisPoolConfig getConfig() {
    JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
    jedisPoolConfig.setMaxIdle(maxIdle);
    jedisPoolConfig.setMaxTotal(maxActive);
    jedisPoolConfig.setMinIdle(minIdle);
    return jedisPoolConfig;
  }

}
