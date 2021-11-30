package com.plumekanade.robot.utils;

import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.vo.*;
import com.plumekanade.robot.constants.DateConst;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static com.plumekanade.robot.constants.ProjectConst.WRAP;

/**
 * 转换类
 *
 * @author kanade
 * @version 1.0
 * @date 2021-04-27 19:21:48
 */
@Slf4j
public class ConvertUtils {

  /**
   * 计算流大小是否大于1MB
   */
  public static boolean validOneMB(int byteSize) {
    return (byteSize / ProjectConst.MULTIPLE) >= ProjectConst.MULTIPLE;
  }

  /**
   * 格式化原神账号信息查询数据
   *
   * @date 2021-07-30 00:51
   */
  public static String formatInfoData(String gameId, String channel, GenshinInfo genshinInfo) {
    StringBuilder builder = new StringBuilder("账号 " + gameId + " 的信息：\n角色：\n");
    // 获取人物信息
    List<Long> characterIds = new ArrayList<>(genshinInfo.getAvatars().size());
    genshinInfo.getAvatars().forEach(item -> characterIds.add(item.getId()));
    List<GenshinCharacterInfo> characterInfo = MiHoYoUtils.getCharacterInfo(characterIds, gameId, channel);
    // 按稀有度&命座排序
    genshinInfo.getAvatars().sort(Comparator.comparingInt(GenshinCharacter::getRarity)
        .thenComparing(GenshinCharacter::getActived_constellation_num).reversed());
    for (GenshinCharacter character : genshinInfo.getAvatars()) {

      builder.append(character.getActived_constellation_num()).append("命").append(character.getName())
          .append("（lv").append(character.getLevel()).append(" ").append("好感").append(character.getFetter()).append(" ");

      for (GenshinCharacterInfo info : characterInfo) {
        if (character.getId().equals(info.getId())) {
          builder.append("lv").append(info.getWeapon().getLevel()).append("精炼")
              .append(info.getWeapon().getAffix_level()).append(info.getWeapon().getName());
//              .append(info.getWeapon().getAffix_level()).append(info.getWeapon().getName()).append("-");
//          for (GenshinCharacterInfo.Reliquary reliquary : info.getReliquaries()) {
//            builder.append("lv").append(reliquary.getLevel()).append(reliquary.getName())
//                .append("(").append(reliquary.getPos_name()).append(")").append(" ");
//          }
//          builder.replace(builder.length() - 1, builder.length(), "）\n");
          builder.append("）\n");
          characterInfo.remove(info);
          break;
        }
      }

//      builder.append(character.getRarity()).append("☆·").append(GodEyeEnum.valueOf(character.getElement()).getValue()).append("·")
//              .append(character.getName()).append("（").append(character.getLevel()).append("级 ").append(character.getFetter()).append("级好感 ")
//              .append(character.getActived_constellation_num()).append("命").append("）\n");
    }
    GenshinAccountInfo accountInfo = genshinInfo.getStats();
    builder.append("\n账号数据总览：\n");
    builder.append("活跃天数：").append(accountInfo.getActive_day_number()).append(WRAP)
        .append("成就达成数：").append(accountInfo.getAchievement_number()).append(WRAP)
        .append("获得角色数：").append(accountInfo.getAvatar_number()).append(WRAP)
        .append("解锁传送点：").append(accountInfo.getWay_point_number()).append(WRAP)
        .append("风神瞳：").append(accountInfo.getAnemoculus_number()).append(WRAP)
        .append("岩神瞳：").append(accountInfo.getGeoculus_number()).append(WRAP)
        .append("雷神瞳：").append(accountInfo.getElectroculus_number()).append(WRAP)
        .append("解锁秘境：").append(accountInfo.getActive_day_number()).append(WRAP)
        .append("深境螺旋：").append(accountInfo.getSpiral_abyss()).append(WRAP)
        .append("华丽宝箱数：").append(accountInfo.getLuxurious_chest_number()).append(WRAP)
        .append("珍贵宝箱数：").append(accountInfo.getPrecious_chest_number()).append(WRAP)
        .append("精致宝箱数：").append(accountInfo.getExquisite_chest_number()).append(WRAP)
        .append("普通宝箱数：").append(accountInfo.getCommon_chest_number()).append("\n\n");


    List<GenshinHome> homes = genshinInfo.getHomes();
    if (homes.size() > 0) {
      builder.append("尘歌壶洞天：\n");
      for (GenshinHome home : homes) {
        builder.append(home.getName()).append("、");
      }
      GenshinHome home = homes.get(0);
      builder.replace(builder.length() - 1, builder.length(), "信任等级：").append(home.getLevel()).append(WRAP);
      builder.append("历史访客数：").append(home.getVisit_num()).append(WRAP);
      builder.append("最高洞天仙力：").append(home.getComfort_num())
          .append("（").append(home.getComfort_level_name()).append("）\n");
      builder.append("获得摆设数：").append(home.getItem_num()).append(WRAP);
    } else {
      builder.append("尘歌壶洞天：好像还没开启");
    }

    builder.append(WRAP).append(WRAP).append(MiHoYoUtils.getSpiralAbyss(gameId));

    return builder.toString();
  }

  /**
   * 处理深境螺旋数据记录
   */
  public static void handleAbyssRecord(StringBuilder builder, GenshinSpiralAbyss abyss) {
    if (null == abyss) {
      builder.append("深境螺旋接口异常！\n");
      return;
    }
//    if (abyss.is_unlock()) {
//      builder.append("账号未解锁深境螺旋玩法！\n");
//      return;
//    }
    if ("0-0".equals(abyss.getMax_floor())) {
      builder.append("好像并没有打哦~~\n");
      return;
    }

    final String spacer = " - ";
    builder.append(DateConst.SMALL_SDF.format(new Date(abyss.getStart_time() * 1000)))
        .append(spacer).append(DateConst.SMALL_SDF.format(new Date(abyss.getEnd_time() * 1000))).append(WRAP)
        .append("最深抵达：").append(abyss.getMax_floor()).append(WRAP)
        .append("总胜利场次：").append(abyss.getTotal_win_times()).append(WRAP)
        .append("战斗次数：").append(abyss.getTotal_battle_times()).append(ProjectConst.SPACE)
        .append("总星数：").append(abyss.getTotal_star()).append(WRAP)
        .append("最多出战：");

    abyss.getReveal_rank().forEach(rank ->
        builder.append(ProjectConst.GENSHIN_AVATAR_MAP.get(rank.getAvatar_id()).getName())
            .append(spacer).append(rank.getValue()).append("次").append(ProjectConst.SPACE));

    AbyssCommonRank defeatRank = abyss.getDefeat_rank().get(0);
    AbyssCommonRank damageRank = abyss.getDamage_rank().get(0);
    AbyssCommonRank takeDamageRank = abyss.getTake_damage_rank().get(0);
    AbyssCommonRank normalSkillRank = abyss.getNormal_skill_rank().get(0);
    AbyssCommonRank energySkillRank = abyss.getEnergy_skill_rank().get(0);
    builder.append(WRAP)
        .append("最多击败数：").append(ProjectConst.GENSHIN_AVATAR_MAP.get(defeatRank.getAvatar_id()).getName())
        .append(spacer).append(defeatRank.getValue()).append(WRAP)
        .append("最大伤害：").append(ProjectConst.GENSHIN_AVATAR_MAP.get(damageRank.getAvatar_id()).getName())
        .append(spacer).append(damageRank.getValue()).append(WRAP)
        .append("最大承伤：").append(ProjectConst.GENSHIN_AVATAR_MAP.get(takeDamageRank.getAvatar_id()).getName())
        .append(spacer).append(takeDamageRank.getValue()).append(WRAP)
        .append("使用最多元素战技：").append(ProjectConst.GENSHIN_AVATAR_MAP.get(normalSkillRank.getAvatar_id()).getName())
        .append(spacer).append(normalSkillRank.getValue()).append("次").append(WRAP)
        .append("使用最多元素爆发：").append(ProjectConst.GENSHIN_AVATAR_MAP.get(energySkillRank.getAvatar_id()).getName())
        .append(spacer).append(energySkillRank.getValue()).append("次").append(WRAP);
  }

}
