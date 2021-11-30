package com.plumekanade.robot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.plumekanade.robot.entity.Gallery;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-08-24 14:30
 */
@Repository
public interface GalleryMapper extends BaseMapper<Gallery> {

  /**
   * 随机返回1条数据
   *
   * @date 2021-08-27 15:52
   */
  @Select("select * from gallery o join " +
      "(select round(rand() * (select max(id) FROM gallery where sexy_state <= #{sexyState})) as id) o1 " +
      "where o.id >= o1.id and o.sexy_state <= #{sexyState} order by o.id asc limit 1")
  Gallery randomImg(@Param("sexyState") int sexyState);
}
