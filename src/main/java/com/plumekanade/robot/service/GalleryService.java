package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.constants.SysKeyConst;
import com.plumekanade.robot.entity.Gallery;
import com.plumekanade.robot.entity.SystemConfig;
import com.plumekanade.robot.mapper.GalleryMapper;
import com.plumekanade.robot.mapper.SystemConfigMapper;
import com.plumekanade.robot.utils.CommonUtils;
import com.plumekanade.robot.utils.ImageHashUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author kanade
 * @version 1.0
 * @date 2021-08-24 14:32
 */
@Slf4j
@Service
@AllArgsConstructor
public class GalleryService extends ServiceImpl<GalleryMapper, Gallery> {

  private final GalleryMapper galleryMapper;
  private final SystemConfigService systemConfigService;

  /**
   * 从指定路径中读取图片目录加入
   *
   * @date 2021-08-26 16:10
   */
  public void updateGallery(String path, int sexyState) {
    String galleryUrl = systemConfigService.getVal(SysKeyConst.GALLERY_URL) + (sexyState == 0 ? "normal/" : (sexyState == 1 ? "sexy/" : "bare/"));
    try {
      File catalog = new File(path);
      File[] files = catalog.listFiles();
      if (null == files || 0 >= files.length) {
        return;
      }
      List<Gallery> insertList = new ArrayList<>();
      Set<String> names = new HashSet<>();
      List<Gallery> galleries = galleryMapper.selectList(new QueryWrapper<Gallery>().eq("sexy_state", sexyState));
      for (Gallery item : galleries) {
        names.add(item.getFilename());
      }

      for (File file : files) {
        // 是文件且不在数据库
        if (file.isFile() && !names.contains(file.getName())) {
          insertList.add(new Gallery(file.getName(), file.getName(), sexyState, file.getPath(), galleryUrl + file.getName()));
        }
      }
      saveBatch(insertList);
    } catch (Exception e) {
      log.error("【更新图库】从路径 {} 更新图库失败, 堆栈信息: ", path, e);
    }
  }

  /**
   * 随机获取图片
   *
   * @date 2021-08-27 15:15
   */
  public String randomImg(int sexyState) {
    LambdaQueryWrapper<Gallery> wrapper = new LambdaQueryWrapper<>();
    wrapper.le(Gallery::getSexyState, sexyState);

    long idx = CommonUtils.RANDOM.nextLong(count(wrapper));
    long page = idx / 10;
    idx = idx % 10;
    if (idx == 0) {
      page--;
    }

    Gallery gallery = page(new Page<>(page, 10), wrapper).getRecords().get((int) idx);
    return gallery.getPath();
  }
}
