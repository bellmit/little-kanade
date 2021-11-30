package com.plumekanade.robot.utils;

import com.plumekanade.robot.constants.DateConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.util.Date;

/**
 * redis登录信息凭证等工具类
 *
 * @author kanade
 * @date 2021-11-01 10:06
 */
@Slf4j
@Component
public class RedisCertUtils {

  @Resource(name = "redisZero")
  private JedisPool redisCert;

  private static final String WECHAT_USER = "wxUser:";
  private static final String RANDOM_IMG_COOL = "randomImgCool:";

  /**
   * 存放微信用户信息映射 openid -> nickname
   */
  public void setWxUserName(String openid, String nickname) {
    try (Jedis jedis = redisCert.getResource()) {
      jedis.set(WECHAT_USER + openid, nickname, SetParams.setParams().ex(DateConst.WEEK_SECONDS));
    } catch (Exception e) {
      log.error("【Redis】存放微信用户 {} - {} 信息异常, 堆栈信息: ", openid, nickname, e);
    }
  }

  /**
   * 获取微信用户名称
   */
  public String getWxUserName(String openid) {
    try (Jedis jedis = redisCert.getResource()) {
      return jedis.get(WECHAT_USER + openid);
    } catch (Exception e) {
      log.error("【Redis】获取微信用户 {} 信息异常, 堆栈信息: ", openid, e);
    }
    return null;
  }

  /**
   * 设置随机图片冷却时间
   * @param code 群号/用户号
   */
  public void setRandomImgCoolTime(String code) {
    try(Jedis jedis = redisCert.getResource()) {
      jedis.set(RANDOM_IMG_COOL + code, String.valueOf(new Date().getTime() + 60000), SetParams.setParams().ex(DateConst.SIXTY));
    } catch (Exception e) {
      log.error("【Redis】设置随机图片冷却时间失败, 堆栈信息: ", e);
    }
  }

  /**
   * 判断是否处于冷却中
   */
  public Long isRandomImgCooling(String code) {
    try(Jedis jedis = redisCert.getResource()) {
      String cool = jedis.get(RANDOM_IMG_COOL + code);
      return cool == null ? null : Long.parseLong(cool);
    } catch (Exception e) {
      log.error("【Redis】判断随机图片是否冷却中出现异常, 堆栈信息: ", e);
    }
    return null;
  }

}
