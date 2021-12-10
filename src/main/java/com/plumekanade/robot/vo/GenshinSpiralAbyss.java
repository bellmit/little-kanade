package com.plumekanade.robot.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 深境螺旋
 *
 * @author kanade
 * @version 1.0
 * @date 2021-08-07 16:19
 */
@Data
@JsonPropertyOrder(alphabetic = true)
public class GenshinSpiralAbyss implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  private Integer schedule_id;
  private Long start_time;
  private Long end_time;
  // 总战斗次数
  private Integer total_battle_times;
  // 总胜利次数
  private Integer total_win_times;
  // 最深到达
  private String max_floor;
  // 最多出战
  private List<AbyssCommonRank> reveal_rank;
  // 最多击败
  private List<AbyssCommonRank> defeat_rank;
  // 最大伤害
  private List<AbyssCommonRank> damage_rank;
  // 承受最多伤害
  private List<AbyssCommonRank> take_damage_rank;
  // 使用最多元素战技
  private List<AbyssCommonRank> normal_skill_rank;
  // 使用大招最多
  private List<AbyssCommonRank> energy_skill_rank;
  // 9~12层的战斗记录
  private List<Object> floors;
  // 总星数
  private Integer total_star;
  // 是否解锁深境螺旋玩法 ? 不确定
  private boolean is_unlock;


}
