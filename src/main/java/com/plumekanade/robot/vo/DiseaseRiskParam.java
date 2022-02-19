package com.plumekanade.robot.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 疫情风险地区查询参数VO
 * <p>
 *   function s(t) {
 *   var e = ((new Date).getTime() / 1e3).toFixed(), a = '23y0ufFl5YxIyGrI8hWRUZmKkvtSjLQA', i = '123456789abcdefg', s = 'zdww'
 *   return JSON.stringify(Object.assign({
 *     appId: 'NcApplication',
 *     paasHeader: s,
 *     timestampHeader: e,
 *     nonceHeader: i,
 *     signatureHeader: CryptoJS.SHA256(e + a + i + e).toString(CryptoJS.enc.Hex).toUpperCase()
 *   }, t))
 * }
 * </p>
 * @version 1.0
 * @author kanade
 * @date 2022-02-19 11:24
 */
@Data
public class DiseaseRiskParam implements Serializable {
  @Serial
  private static final long serialVersionUID = 1L;

  /**
   * 固定
   */
  private String appId = "NcApplication";
  /**
   * 固定
   */
  private String paasHeader = "zdww";
  /**
   * 秒 时间戳
   */
  private String timestampHeader;
  /**
   * 固定
   */
  private String nonceHeader = "123456789abcdefg";
  /**
   * 签名 具体看上方js签名逻辑
   */
  private String signatureHeader;
  /**
   * key 与签名逻辑一致
   */
  private String key;

}
