package com.plumekanade.robot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.constants.SysKeyConst;
import com.plumekanade.robot.entity.Gallery;
import com.plumekanade.robot.entity.SystemConfig;
import com.plumekanade.robot.mapper.GalleryMapper;
import com.plumekanade.robot.mapper.SystemConfigMapper;
import com.plumekanade.robot.utils.CommonUtils;
import com.plumekanade.robot.utils.ImageHashUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.*;

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
  public String randomImg(int sexyState, List<String> params) {
    LambdaQueryWrapper<Gallery> wrapper = new LambdaQueryWrapper<>();
    wrapper.le(Gallery::getSexyState, sexyState);
    if (null != params) {
      for (String param : params) {
        String[] strings = param.split(ProjectConst.VERTICAL);
        wrapper.and(w -> {
          for (int i = 0; i < strings.length; i++) {
            if (i > 0) {
              w.or();
            }
            w.like(Gallery::getTags, strings[i]);
          }
        });
      }
    }

    long count = count(wrapper);
    if (count == 0) {
      return null;
    }
    long idx = CommonUtils.RANDOM.nextLong();
    long page = idx / 10;
    idx = idx % 10;
    if (idx == 0) {
      page--;
    }

    Gallery gallery = page(new Page<>(page, 10), wrapper).getRecords().get((int) idx);
    return gallery.getPath();
  }

  /**
   * 根据path获取对应图片
   */
  public Gallery getImage(String path) {
    return baseMapper.selectOne(new LambdaQueryWrapper<Gallery>().eq(Gallery::getPath, path));
  }

  /**
   * 压缩图库的图片
   */
  @Transactional(rollbackFor = Exception.class)
  public Boolean compressImg() {
    LambdaQueryWrapper<Gallery> wrapper = new LambdaQueryWrapper<>();
    // 2MB左右 209????
    wrapper.gt(Gallery::getSize, 2100000);
    List<Gallery> list = list(wrapper);
    if (list.size() > 0) {
      File file;
      File compressFile;

      for (Gallery gallery : list) {
        String path = gallery.getPath();
        file = new File(path);

        // 图片不存在
        if (!file.exists()) {
          removeById(gallery);
          continue;
        }

        try {
          if (path.contains(ProjectConst.JPG)) {
            gallery.setSize(compress(0.96f, file, file));
          } else {
            gallery.setUrl(gallery.getUrl().replaceAll(ProjectConst.PNG, ProjectConst.JPG));
            gallery.setFilename(gallery.getFilename().replaceAll(ProjectConst.PNG, ProjectConst.JPG));
            path = path.replaceAll(ProjectConst.PNG, ProjectConst.JPG);
            compressFile = new File(path);
            gallery.setSize(compress(0.96f, file, compressFile));
            // 移除原有的png文件
            log.info("【图片压缩】移除原 png 文件: {}", file.delete());
          }
        } catch (Exception e) {
          log.error("【图片压缩】压缩图片出错, 异常堆栈: ", e);
        }
      }
      updateBatchById(list);
    }
    return true;
  }

  /**
   * 递归压缩图片直至小于2M
   */
  public long compress(float quality, File compressFile, File targetFile) throws Exception {
    long size = compressFile.length();
    if (size / 1024 > 2048) {
      Thumbnails.of(compressFile).size(2560, 2560).outputQuality(quality).toFile(targetFile);
      quality -= 0.03f;
      size = compress(quality, new File(targetFile.getPath()), targetFile);
    }
    return size;
  }

}
