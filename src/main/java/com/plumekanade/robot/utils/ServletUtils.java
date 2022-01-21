package com.plumekanade.robot.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plumekanade.robot.constants.APIConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.entity.RandomEmoticon;
import com.plumekanade.robot.enums.CodeEnum;
import com.plumekanade.robot.vo.LoLiConResult;
import com.plumekanade.robot.vo.MiHoYoEmoticon;
import com.plumekanade.robot.vo.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * 请求工具类
 *
 * @author kanade
 * @version 1.0
 * @date 2021-04-17 16:57:41
 */
@Slf4j
public class ServletUtils {

  /**
   * 通用get
   */
  public static HttpEntity get(String url) throws Exception {
    // 创建Httpclient对象
    CloseableHttpClient httpclient = HttpClients.createDefault();
    // 创建http GET 并 发起请求
    CloseableHttpResponse response = httpclient.execute(new HttpGet(url));
    // 判断返回状态是否为200
    if (ProjectConst.SUCCESS.equals(response.getStatusLine().getStatusCode())) {
      return response.getEntity();
    }
    return null;
  }

  /**
   * 通用get
   */
  public static HttpEntity getWithHeader(String url, Header header) throws Exception {
    // 创建Httpclient对象
    CloseableHttpClient httpclient = HttpClients.createDefault();
    // 创建http GET 并 发起请求
    HttpGet httpGet = new HttpGet(url);
    // 设置请求头
    httpGet.setHeader(header);
    CloseableHttpResponse response = httpclient.execute(httpGet);
    // 判断返回状态是否为200
    if (ProjectConst.SUCCESS.equals(response.getStatusLine().getStatusCode())) {
      return response.getEntity();
    }
    return null;
  }

  /**
   * 需要加入头部的GET请求
   */
  public static String get(String url, Header header) throws Exception {
    // 创建Httpclient对象
    CloseableHttpClient httpclient = HttpClients.createDefault();
    // 创建http GET 并 发起请求
    HttpGet httpGet = new HttpGet(url);
    // 设置请求头
    httpGet.setHeader(header);
    CloseableHttpResponse response = httpclient.execute(httpGet);
    // 判断返回状态是否为200
    if (ProjectConst.SUCCESS.equals(response.getStatusLine().getStatusCode())) {
      return EntityUtils.toString(response.getEntity());
    }
    return null;
  }

  /**
   * 需要加入头部的GET请求
   */
  public static String get(String url, Header[] header) throws Exception {
    // 创建Httpclient对象
    CloseableHttpClient httpclient = HttpClients.createDefault();
    // 创建http GET 并 发起请求
    HttpGet httpGet = new HttpGet(url);
    // 设置请求头
    httpGet.setHeaders(header);
    CloseableHttpResponse response = httpclient.execute(httpGet);
    // 判断返回状态是否为200
    if (ProjectConst.SUCCESS.equals(response.getStatusLine().getStatusCode())) {
      return EntityUtils.toString(response.getEntity());
    }
    return null;
  }

  /**
   * 需要加入头部的POST请求
   */
  public static String post(String url, String params, Header[] headers) throws Exception {
    // 创建Httpclient对象
    CloseableHttpClient httpclient = HttpClients.createDefault();
    // 创建http POST 并 发起请求
    HttpPost httpPost = new HttpPost(url);
    httpPost.setHeaders(headers);
    // 设置参数
    StringEntity entity = new StringEntity(params, StandardCharsets.UTF_8);
    entity.setContentEncoding(StandardCharsets.UTF_8.toString());
    // 发送Json格式的数据请求
    httpPost.setHeader(APIConst.CONTENT_TYPE, APIConst.CONTENT_TYPE_JSON);
    httpPost.setEntity(entity);
    CloseableHttpResponse response = httpclient.execute(httpPost);
    // 判断返回状态是否为200
    if (ProjectConst.SUCCESS.equals(response.getStatusLine().getStatusCode())) {
      return EntityUtils.toString(response.getEntity());
    }
    return null;
  }

  /**
   * 普通get
   */
  public static synchronized String normalGet(String url) {
    try {
      return EntityUtils.toString(Objects.requireNonNull(get(url)));
    } catch (Exception e) {
      log.error("【Get】普通Get请求异常: ", e);
    }
    return "";
  }

  /**
   * 需要unicode转中文的get
   */
  public static synchronized Map<String, String> convertGet(String url) {
    try {
      return MapperUtils.deserialize(Objects.requireNonNull(get(url)).getContent(), new TypeReference<>() {
      });
    } catch (Exception e) {
      log.error("【Get】需要 ASCII 转中文的Get请求异常: ", e);
    }
    return new HashMap<>(0);
  }

//  /**
//   * 图片地址使用的方法
//   */
//  public static synchronized void fileGet(String url, ChatImageService chatImageService) {
//    try {
//
//      InputStream is = Objects.requireNonNull(get(url)).getContent();
//      ByteArrayOutputStream os = new ByteArrayOutputStream();
//      byte[] buffer = new byte[1024];     // 1kb
//      int len;
//      while ((len = is.read(buffer)) != -1) {
//        os.write(buffer, 0, len);
//      }
//      byte[] bytes = os.toByteArray();
//
//      // 保存图片 根据图片是否大于1024kb决定保存路径
//      String filename = CommonUtils.id() + ".png";
//      boolean hqFlg = ConvertUtils.validOneMB(bytes.length);
//      String path = (hqFlg ? ProjectConst.HQ_IMG_PATH : ProjectConst.LQ_IMG_PATH) + filename;
//      try (FileOutputStream fos = new FileOutputStream(path)) {
//        fos.write(bytes);
//        String hash = ImageHashUtils.getHash(new File(path));
//        ChatImage image = chatImageService.getImage(hash);
//        if (null == image) {
//          chatImageService.save(new ChatImage(filename, hqFlg ? 1 : 0, path, hash,
//              ProjectConst.CHAT_IMG_URL + (hqFlg ? "hqImage/" : "lqImage/") + filename));
//        }
//      } catch (Exception e) {
//        log.error("【群聊图片】保存图片到服务器失败, 堆栈信息: ", e);
//      }
//      os.close();
//      is.close();
//    } catch (Exception e) {
//      log.error("【Get】获取图片请求失败: ", e);
//    }
//  }

  /**
   * 渲染到客户端 一般用到这个都是返回错误信息
   *
   * @param codeEnum 返回数据
   */
  public static void render(HttpServletResponse response, CodeEnum codeEnum) {

    // 允许跨域
    response.setHeader("Access-Control-Allow-Origin", "*");
    // 允许自定义请求头token(允许head跨域)
    response.setHeader("Access-Control-Allow-Headers", "kanade-token, Accept, Origin, X-Requested-With, " +
        "Content-Type, Last-Modified, Access-Control-Request-Headers, Access-Control-Request-Method");
    response.setHeader("Access-Control-Expose-Headers", "kanade-token");
    response.setHeader("Access-Control-Allow-Credentials", "true");
    response.setHeader(APIConst.CONTENT_TYPE, APIConst.CONTENT_TYPE_JSON_CHARSET);
    response.setStatus(codeEnum.getCode());
    try {
      response.getWriter().print(codeEnum.getMsg());
    } catch (Exception e) {
      log.error("render方法响应异常: ", e);
    }
  }

  /**
   * 获取米游社表情
   */
  public static List<RandomEmoticon> getMiYouSheEmoticon() {
    List<RandomEmoticon> emoticons = new ArrayList<>();
    String get = normalGet(APIConst.EMOTICON);
    TypeReference<Map<String, Object>> typeReference = new TypeReference<>() {
    };
    Map<String, Object> result = MapperUtils.deserialize(get, typeReference);
    result = MapperUtils.deserialize(MapperUtils.serialize(Objects.requireNonNull(result).get("data")), typeReference);
    List<MiHoYoEmoticon> list = MapperUtils.deserialize(MapperUtils.serialize(Objects.requireNonNull(result).get("list")), new TypeReference<>() {
    });
    File file;
    InputStream is;
    ByteArrayOutputStream os;
    FileOutputStream fos;
    RandomEmoticon emoticon;
    assert list != null;
    for (MiHoYoEmoticon miHoYoEmoticon : list) {
      List<MiHoYoEmoticon> items = miHoYoEmoticon.getList();
      if (items.size() <= 0) {
        continue;
      }
      try {
        for (MiHoYoEmoticon item : items) {
          file = new File("/home/mirai/image/" + miHoYoEmoticon.getName() + "/" + item.getName() + ".png");
//          file = new File("D:\\kanade\\github\\mirai-kanade\\image\\" + miHoYoEmoticon.getName() + "\\" + item.getName() + ".png");
          if (!file.getParentFile().exists()) { // 创建目录
            file.getParentFile().mkdirs();
          }
          if (file.exists()) {  // 跳过已有
            continue;
          }
          is = Objects.requireNonNull(get(item.getIcon())).getContent();
          os = new ByteArrayOutputStream();
          byte[] buffer = new byte[1024];     // 1kb
          int len;
          while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
          }
          is.close();
          // 写入
          fos = new FileOutputStream(file);
          fos.write(os.toByteArray());
          os.close();
          emoticon = new RandomEmoticon(file.getPath());
          emoticons.add(emoticon);
        }
      } catch (Exception e) {
        log.error("【Get】获取图片请求失败: ", e);
      }
    }
    return emoticons;
  }

  /**
   * LoLiCon的API请求处理
   */
  public static List<LoLiConResult> handleLoLiConReq(String params) {
    try {
      // 创建http POST 并 发起请求
      HttpPost httpPost = new HttpPost(APIConst.LO_LI_CON_IMG);
      // 设置参数
      StringEntity entity = new StringEntity(params, StandardCharsets.UTF_8);
      // 发送Json格式的数据请求
      entity.setContentType(APIConst.CONTENT_TYPE_JSON);
      httpPost.setHeader(APIConst.CONTENT_TYPE, APIConst.CONTENT_TYPE_JSON);
      httpPost.setEntity(entity);
      CloseableHttpResponse response = HttpClients.createDefault().execute(httpPost);
      // 判断返回状态是否为200
      if (ProjectConst.SUCCESS.equals(response.getStatusLine().getStatusCode())) {
        ResultMsg resultMsg = MapperUtils.deserialize(EntityUtils.toString(response.getEntity()), ResultMsg.class);
        if (null != resultMsg) {
          return MapperUtils.deserialize(MapperUtils.serialize(resultMsg.getData()), new TypeReference<>() {
          });
        }
      }
    } catch (Exception e) {
      log.error("【POST】LoLiCon的API请求出错, 堆栈信息: ", e);
    }
    return new ArrayList<>();
  }

}
