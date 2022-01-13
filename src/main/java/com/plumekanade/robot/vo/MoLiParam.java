package com.plumekanade.robot.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 茉莉API相关VO
 * @version 1.0
 * @author kanade
 * @date 2022-01-13 16:18
 */
public class MoLiParam {

  /**
   * 请求参数
   */
  @Data
  @NoArgsConstructor
  public static class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 消息主体，跟机器人交互的文本（长度超过64个字符将被自动截取，只保留前64个字符）
     */
    private String content;
    /**
     * 对话场景，1：私聊，2：群聊（对话场景不一样，from和to参数的含义也不一样）
     */
    private Integer type = 2;
    /**
     * 消息发送者标识符（ID）：群消息时，此值表示群成员；好友消息时，此值表示好友。（长度超过32个字符将被自动截取）
     */
    private Long from;
    /**
     * 消息发送者名字或昵称：群消息时，此值表示群成员；好友消息时，此值表示好友。（长度超过32个字符将被自动截取）
     */
    private String fromName;
    /**
     * 消息接收者标识符（ID）：群消息时此值表示群标识；好友消息时此值不用传，并且无效。（长度超过32个字符将被自动截取）
     */
    private Long to;
    /**
     * 消息接收者名字或昵称：群消息时此值表示群名；好友消息时此值不用传，并且无效。（长度超过32个字符将被自动截取）
     */
    private String toName;

    public Request(String content, Long from, String fromName, Long to, String toName) {
      this.content = content;
      this.from = from;
      this.fromName = fromName;
      this.to = to;
      this.toName = toName;
    }
  }

  /**
   * 返回数据解析
   */
  @Data
  public static class Result implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 响应码，此值非00000都为异常状态码
     */
    private String code;
    /**
     * 接口响应的消息提示，异常状态码可以参考此值的消息提示
     */
    private String message;
    /**
     * 此次调用的最终处理插件，有Chat、Weather、Translation等，详细见下面插件列举，如果为null则表示是万金油回复
     */
    private String plugin;
    /**
     * 当接口响应码为00000时，就会响应接口的正确数据
     */
    private List<ResultItem> data;

  }

  /**
   * 返回数据解析的结果列表对象
   */
  @Data
  public static class ResultItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    /**
     * 回复的类型, 1文本, 2图片, 3文档, 4音频, 9其它文件
     */
    private Integer typed;
    /**
     * 备注？
     */
    private String remark;
    /**
     * 回复内容
     * 当typed=2，content=lib/image/20211101/202111011822283300.jpg，
     * 则这个文件的真实访问地址是https://files.molicloud.com/lib/image/20211101/202111011822283300.jpg
     */
    private String content;
  }

}
