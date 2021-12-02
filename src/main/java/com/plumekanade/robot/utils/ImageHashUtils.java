package com.plumekanade.robot.utils;

import com.plumekanade.robot.constants.ProjectConst;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;

/**
 * 图片处理
 *
 * @author kanade
 * @version 1.0
 * @date 2021-08-24 14:39
 */
@Slf4j
public class ImageHashUtils {

  private static final int SIZE = 32;
  private static final int SMALLER_SIZE = 8;
  private static final double[] c = {1 / Math.sqrt(2.0), 1, 1, 1, 1, 1, 1, 1, 1, 1,
      1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};

  /**
   * 返回二进制字符串，类似“001010111011100010”，可用于计算汉明距离
   */
  public static String getHash(File file) throws Exception {

    BufferedImage image = grayscale(resize(ImageIO.read(file)));

    double[][] val = new double[SIZE][SIZE];
    for (int x = 0; x < image.getWidth(); x++) {
      for (int y = 0; y < image.getHeight(); y++) {
        val[x][y] = getBlue(image, x, y);
      }
    }

    double[][] dctVal = applyDCT(val);
    double total = 0;
    for (int x = 0; x < SMALLER_SIZE; x++) {
      for (int y = 0; y < SMALLER_SIZE; y++) {
        total += dctVal[x][y];
      }
    }

    total -= dctVal[0][0];
    double avg = total / (double) ((SMALLER_SIZE * SMALLER_SIZE) - 1);

    StringBuilder hash = new StringBuilder();
    for (int x = 0; x < SMALLER_SIZE; x++) {
      for (int y = 0; y < SMALLER_SIZE; y++) {
        if (x != 0 && y != 0) {
          hash.append((dctVal[x][y] > avg ? ProjectConst.ONE : ProjectConst.ZERO));
        }
      }
    }

    return hash.toString();
  }

  /**
   * 重新缩放图片
   *
   * @date 2021-08-24 14:55
   */
  private static BufferedImage resize(BufferedImage image) {
    BufferedImage resizedImage = new BufferedImage(SIZE, SIZE, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = resizedImage.createGraphics();
    graphics.drawImage(image, 0, 0, SIZE, SIZE, null);
    graphics.dispose();
    return resizedImage;
  }

  /**
   * 灰度
   *
   * @date 2021-08-24 14:56
   */
  private static BufferedImage grayscale(BufferedImage img) {
    new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null).filter(img, img);
    return img;
  }

  private static int getBlue(BufferedImage img, int x, int y) {
    return (img.getRGB(x, y)) & 0xff;
  }


  private static double[][] applyDCT(double[][] f) {

    double[][] F = new double[SIZE][SIZE];
    for (int u = 0; u < SIZE; u++) {
      for (int v = 0; v < SIZE; v++) {
        double sum = 0.0;
        for (int i = 0; i < SIZE; i++) {
          for (int j = 0; j < SIZE; j++) {
            sum += Math.cos(((2 * i + 1) / (2.0 * SIZE)) * u * Math.PI)
                * Math.cos(((2 * j + 1) / (2.0 * SIZE)) * v * Math.PI) * (f[i][j]);
          }
        }
        sum *= ((c[u] * c[v]) / 4.0);
        F[u][v] = sum;
      }
    }
    return F;
  }

}
