package com.plumekanade.robot.utils;

import com.plumekanade.robot.constants.DateConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.entity.Tarot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.SetParams;

import javax.annotation.Resource;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

/**
 * bot聊天相关缓存工具类
 *
 * @author kanade
 * @version 1.0
 * @date 2021-11-27 15:11:47
 */
@Slf4j
@Component
public class RedisChatUtils {

  @Resource(name = "redisOne")
  private JedisPool redisChat;

  // 复读相关前缀
  private static final String REPEAT = "repeatChat:";
  private static final String COOLING = "cooling:";
  // 塔罗牌前缀
  private static final String TAROT = "tarot:";
  // 将要生气
  private static final String ANGRY = "angry:";
  // 机器人被禁言状态
  private static final String MUTE = "mute:";
  // 当天活跃状态
  private static final String SIGN_STATE = "signState:";

  /**
   * 设置复读语句
   */
  public void setRepeatRecord(String groupCode, String msg) {
    try (Jedis jedis = redisChat.getResource()) {
      jedis.set(REPEAT + groupCode, msg);
    } catch (Exception e) {
      log.error("【Redis】设置复读记录失败, 堆栈信息: ", e);
    }
  }

  /**
   * 获取复读语句
   */
  public String getRepeatRecord(String groupCode) {
    try (Jedis jedis = redisChat.getResource()) {
      return jedis.get(REPEAT + groupCode);
    } catch (Exception e) {
      log.error("【Redis】获取复读记录失败, 堆栈信息: ", e);
    }
    return "";
  }

  /**
   * 获取是否有复读冷却
   */
  public boolean isRepeatCooling(String groupCode) {
    try (Jedis jedis = redisChat.getResource()) {
      return jedis.exists(REPEAT + COOLING + groupCode);
    } catch (Exception e) {
      log.error("【Redis】获取复读冷却失败, 堆栈信息: ", e);
    }
    return false;
  }

  /**
   * 设置复读冷却 随机冷却时间 1~20分钟
   */
  public void setRepeatCooling(String groupCode) {
    try (Jedis jedis = redisChat.getResource()) {
      jedis.set(REPEAT + COOLING + groupCode, String.valueOf(System.currentTimeMillis()),
          SetParams.setParams().ex(CommonUtils.RANDOM.nextLong(DateConst.TWENTY_MINUTES_S - DateConst.SIXTY) + DateConst.SIXTY));
    } catch (Exception e) {
      log.error("【Redis】获取复读冷却失败, 堆栈信息: ", e);
    }
  }

  /**
   * 设置每日塔罗牌
   */
  public void setTarot(String accountCode, String value) {
    try (Jedis jedis = redisChat.getResource()) {
      // 获取现在到明天00:00:00的时间差(秒)
      Duration duration = Duration.between(LocalDateTime.now(), LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(0, 0, 0)));
      jedis.set(TAROT + accountCode, value, SetParams.setParams().ex(duration.toSeconds()));
    } catch (Exception e) {
      log.error("【Redis】设置账号 {} 每日塔罗失败, 堆栈信息: ", accountCode, e);
    }
  }

  /**
   * 获取每日塔罗牌
   */
  public Tarot getTarot(String accountCode) {
    try (Jedis jedis = redisChat.getResource()) {
      return MapperUtils.deserialize(jedis.get(TAROT + accountCode), Tarot.class);
    } catch (Exception e) {
      log.error("【Redis】获取账号 {} 每日塔罗失败, 堆栈信息: ", accountCode, e);
    }
    return null;
  }

  /**
   * 设置机器人将要生气标记 30~90秒
   */
  public void setWillAngryFlag(String groupId) {
    try (Jedis jedis = redisChat.getResource()) {
      jedis.set(ANGRY + groupId, ProjectConst.ZERO, SetParams.setParams().ex(CommonUtils.RANDOM.nextLong(DateConst.SIXTY) + 30L));
    } catch (Exception e) {
      log.error("【Redis】设置机器人即将生气标记失败, 堆栈信息: ", e);
    }
  }

  /**
   * 获取机器人即将生气标记
   * 0还没生气(未执行生气后相关功能)
   * 1已生气(已执行生气后相关功能)
   */
  public String getWillAngryFlag(Long groupId) {
    try (Jedis jedis = redisChat.getResource()) {
      return jedis.get(ANGRY + groupId);
    } catch (Exception e) {
      log.error("【Redis】获取机器人生气标记失败, 堆栈信息: ", e);
    }
    return null;
  }

  /**
   * 设置机器人已经生气
   */
  public void setAngry(Long groupId) {
    try (Jedis jedis = redisChat.getResource()) {
      jedis.set(ANGRY + groupId, ProjectConst.ONE, SetParams.setParams().ex(DateConst.SIXTY * 10));
    } catch (Exception e) {
      log.error("【Redis】设置机器人生气标记失败, 堆栈信息: ", e);
    }
  }


  /**
   * 移除机器人生气标记
   */
  public void cancelAngry(Long groupId) {
    try (Jedis jedis = redisChat.getResource()) {
      jedis.del(ANGRY + groupId);
    } catch (Exception e) {
      log.error("【Redis】取消机器人生气标记失败, 堆栈信息: ", e);
    }
  }

  /**
   * 设置机器人被禁言状态
   */
  public void setBotMuteState(Long groupId, int seconds) {
    try (Jedis jedis = redisChat.getResource()) {
      jedis.set(MUTE + groupId, "" + System.currentTimeMillis(), SetParams.setParams().ex(Long.parseLong("" + seconds)));
    } catch (Exception e) {
      log.error("【Redis】设置机器人被禁言状态失败, 堆栈信息: ", e);
    }
  }

  /**
   * 移除机器人被禁言状态
   */
  public void delBotMuteState(Long groupId) {
    try (Jedis jedis = redisChat.getResource()) {
      jedis.del(MUTE + groupId);
    } catch (Exception e) {
      log.error("【Redis】移除机器人被禁言状态失败, 堆栈信息: ", e);
    }
  }

  /**
   * 判断机器人是否被禁言
   */
  public boolean isBotMuted(Long groupId) {
    try (Jedis jedis = redisChat.getResource()) {
      return jedis.exists(MUTE + groupId);
    } catch (Exception e) {
      log.error("【Redis】判断机器人是否被禁言失败, 堆栈信息: ", e);
    }
    return false;
  }

  /**
   * 设置用户活跃状态
   */
  public void setSignState(Long qq) {
    try (Jedis jedis = redisChat.getResource()) {
      jedis.set(SIGN_STATE + qq, System.currentTimeMillis() + "", SetParams.setParams().ex(CommonUtils.getTodaySurplusSeconds()));
    } catch (Exception e) {
      log.error("【Redis】设置用户活跃状态失败, 堆栈信息: ", e);
    }
  }

  /**
   * 获取用户活跃状态
   * @return 已存在 -> false 不存在 -> true
   */
  public boolean isUnSign(Long qq) {
    try (Jedis jedis = redisChat.getResource()) {
      return !jedis.exists(SIGN_STATE + qq);
    } catch (Exception e) {
      log.error("【Redis】获取用户活跃状态失败, 堆栈信息: ", e);
    }
    return true;
  }

}
