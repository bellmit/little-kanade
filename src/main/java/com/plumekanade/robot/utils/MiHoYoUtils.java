package com.plumekanade.robot.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.plumekanade.robot.constants.APIConst;
import com.plumekanade.robot.constants.ProjectConst;
import com.plumekanade.robot.enums.UA;
import com.plumekanade.robot.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @author kanade
 * @date 2021-05-16 21:58:37
 */
@Slf4j
public class MiHoYoUtils {
  //  private static final String version = "2.8.0";  // 2.3.0
//  private static final String salt = "dmq2p7ka6nsu0d3ev6nex4k1ndzrnfiy";  // h8w582wxwgqvahcdkpvdhbh2w9casgfl
  public static String COOKIE;
//    public Result<MiHoYoEntity> login(String account, String password) throws Exception {
//        JSONObject beforeJsonObject = OkHttpUtils.getJson("https://webapi.account.mihoyo.com/Api/create_mmt?scene_type=1&now=" +
//                System.currentTimeMillis() + "&reason=bbs.mihoyo.com", OkHttpUtils.addUA(UA.PC));
//        JSONObject dataJsonObject = beforeJsonObject.getJSONObject("data").getJSONObject("mmt_data");
//        String challenge = dataJsonObject.getString("challenge");
//        String gt = dataJsonObject.getString("gt");
//        String mmtKey = dataJsonObject.getString("mmt_key");
//        Result<DdOcrPojo> identifyResult = ddOcrCodeLogic.identify(gt, challenge, "https://bbs.mihoyo.com/ys/");
//        if (identifyResult.isFailure()) return Result.failure(identifyResult.getMessage());
//        DdOcrPojo ddOcrPojo = identifyResult.getData();
//        String rsaKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDvekdPMHN3AYhm/vktJT+YJr7cI5DcsNKqdsx5DZX0gDuWFuIjzdwButrIYPNmRJ1G8ybDIF7oDW2eEpm5sMbL9zs9ExXCdvqrn51qELbqj0XxtMTIpaCHFSI50PfPpTFV9Xt/hmyVwokoOXFlAEgCn+QCgGs52bFoYMtyi+xEQIDAQAB";
//        String enPassword;
//        try {
//            enPassword = RSAUtils.encrypt(password, RSAUtils.getPublicKey(rsaKey));
//        } catch (Exception e) {
//            return Result.failure("密码加密失败，请重试！！");
//        }
//        Map<String, String> map = new HashMap<>();
//        map.put("is_bh2", "false");
//        map.put("account", account);
//        map.put("password", enPassword);
//        map.put("mmt_key", mmtKey);
//        map.put("is_crypto", "true");
//        map.put("geetest_challenge", ddOcrPojo.getChallenge());
//        map.put("geetest_validate", ddOcrPojo.getValidate());
//        map.put("geetest_seccode", ddOcrPojo.getSecCode());
//        Response response = OkHttpUtils.post("https://webapi.account.mihoyo.com/Api/login_by_password",
//                map, OkHttpUtils.addUA(UA.PC));
//        JSONObject jsonObject = OkHttpUtils.getJson(response);
//        JSONObject infoDataJsonObject = jsonObject.getJSONObject("data");
//        if (infoDataJsonObject.getInteger("status") != 1) return Result.failure(infoDataJsonObject.getString("msg"));
//        String cookie = OkHttpUtils.getCookie(response);
//        JSONObject infoJsonObject = infoDataJsonObject.getJSONObject("account_info");
//        String accountId = infoJsonObject.getString("account_id");
//        String ticket = infoJsonObject.getString("weblogin_token");
//        JSONObject cookieJsonObject = OkHttpUtils.getJson("https://webapi.account.mihoyo.com/Api/cookie_accountinfo_by_loginticket?login_ticket=" +
//                ticket + "&t=" + System.currentTimeMillis(), OkHttpUtils.addHeaders(cookie, "", UA.PC));
//        String cookieToken = cookieJsonObject.getJSONObject("data").getJSONObject("cookie_info").getString("cookie_token");
//        cookie += "cookie_token=" + cookieToken + "; account_id=" + accountId + "; ";
//        Response loginResponse = OkHttpUtils.post("https://bbs-api.mihoyo.com/user/wapi/login",
//                OkHttpUtils.addJson("{\"gids\":\"2\"}"), OkHttpUtils.addCookie(cookie));
//        loginResponse.close();
//        String finaCookie = OkHttpUtils.getCookie(loginResponse);
//        cookie += finaCookie;
//        MiHoYoEntity miHoYoEntity = new MiHoYoEntity(null, null, account, password, cookie, accountId, ticket, cookieToken);
//        return Result.success(miHoYoEntity);
//    }

  private static String ds() {
    String t = String.valueOf(System.currentTimeMillis() / 1000);
    String r = CommonUtils.random(6);
    String c = DigestUtils.md5DigestAsHex(("salt=" + "h8w582wxwgqvahcdkpvdhbh2w9casgfl" + "&t=" + t + "&r=" + r).getBytes(StandardCharsets.UTF_8));
    return t + "," + r + "," + c;
  }

  /**
   * 签到请求头
   *
   * @date 2021-07-28 11:12
   */
  private static Header[] getSignHeaders(String cookie) {
    Header[] headers = new Header[7];
    headers[0] = new BasicHeader("DS", ds());
    headers[1] = new BasicHeader("x-rpc-app_version", "2.3.0");
    headers[2] = new BasicHeader("x-rpc-client_type", "5");
    headers[3] = new BasicHeader("x-rpc-device_id", UUID.randomUUID().toString());
    headers[4] = new BasicHeader("user-agent", UA.GEN_SHIN_SIGN.getValue());
    headers[5] = new BasicHeader("Referer", "https://webstatic.mihoyo.com/bbs/event/signin-ys/index.html?bbs_auth_required=true&act_id=e202009291139501&utm_source=bbs&utm_medium=mys&utm_campaign=icon");
    headers[6] = new BasicHeader("cookie", cookie);
    return headers;
  }

//    private Result<List<Long>> genShinRoleId(String cookie) throws Exception {
//        JSONObject ssJsonObject = OkHttpUtils.getJson("https://api-takumi.mihoyo.com/binding/api/getUserGameRolesByCookie?game_biz=hk4e_cn",
//                OkHttpUtils.addCookie(cookie));
//        if (ssJsonObject.getInteger("retcode") != 0) return Result.failure(ssJsonObject.getString("message"));
//        JSONArray jsonArray = ssJsonObject.getJSONObject("data").getJSONArray("list");
//        if (jsonArray.size() == 0) return Result.failure("您还没有原神角色！！");
//        List<Long> list = new ArrayList<>();
//        for (int i = 0; i < jsonArray.size(); i++) {
//            JSONObject singleJsonObject = jsonArray.getJSONObject(i);
//            String uid = singleJsonObject.getString("game_uid");
//            list.add(Long.parseLong(uid));
//        }
//        return Result.success(list);
//    }
//

  public static String sign(String cookie) throws Exception {
    String uid = getRoles(cookie, true);
    if (uid == null || uid.contains("角色")) {
      return uid;
    }
    String params = "{\"act_id\":\"e202009291139501\",\"region\":\"cn_gf01\",\"uid\":\"" + uid + "\"}";
    String result = ServletUtils.post(APIConst.GEN_SHIN_SIGN, params, getSignHeaders(cookie));
    log.info("签到返回数据: {}", result);
    MiHoYoResult miHoYoResult = MapperUtils.deserialize(result, MiHoYoResult.class);
    if (miHoYoResult == null) {
      throw new Exception("返回值为空");
    }
    if (0 == miHoYoResult.getRetcode() && ProjectConst.OK.equals(miHoYoResult.getMessage())) {
      return "游戏UID: " + uid + " 签到成功。";
    } else {
      throw new Exception("签到失败");
    }

//    JSONObject jsonObject = null;
//    for (int i = 0; i < jsonArray.size(); i++) {
//      JSONObject singleJsonObject = jsonArray.getJSONObject(i);
//      jsonObject = OkHttpUtils.postJson("https://api-takumi.mihoyo.com/event/bbs_sign_reward/sign",
//              OkHttpUtils.addJson("{\"act_id\":\"e202009291139501\",\"region\":\"cn_gf01\",\"uid\":\"" +
//                      singleJsonObject.getString("game_uid") + "\"}"),
//              OkHttpUtils.addHeaders(headerMap(miHoYoEntity)));
//    }
//    if (jsonObject.getInteger("retcode") == 0) return "签到成功！！";
//    else return jsonObject.getString("message");
  }

  /**
   * 查询角色
   * <pre>
   * {"retcode":0,"message":"OK",
   *   "data":{
   *     "list":[{
   *       "game_biz":"hk4e_cn",
   *       "region":"cn_gf01",
   *       "game_uid":"100029100",
   *       "nickname":"立华奏",
   *       "level":56,
   *       "is_chosen":true,
   *       "region_name":"天空岛",
   *       "is_official":true
   *     }]
   *   }
   * }
   * </pre>
   */
  public static String getRoles(String cookie, boolean isGetId) throws Exception {

    String result = ServletUtils.get(APIConst.GEN_SHIN_ROLE, new BasicHeader("Cookie", cookie));
    log.info("【MiHoYo】获取游戏角色: {}", result);

    MiHoYoResult miHoYoResult = MapperUtils.deserialize(result, MiHoYoResult.class);
    if (miHoYoResult == null || 0 != miHoYoResult.getRetcode()) {
      log.error("【MiHoYo】获取游戏角色返回结果为空或请求异常, 请求返回数据原文: " + result);
      throw new Exception("获取角色失败");
    }

    Map<String, List<GenShinRole>> data = MapperUtils.deserialize(MapperUtils.serialize(miHoYoResult.getData()), new TypeReference<>() {
    });

    List<GenShinRole> list = Objects.requireNonNull(data).get("list");
    if (list == null || list.size() == 0) {
      return "你还没创建角色吧...";
    }

    // 内部服务器使用 获取uid
    if (isGetId) {
      return list.get(0).getGame_uid();
    }

    // 用户查询返回
    StringBuilder resultMsg = new StringBuilder();
    for (GenShinRole role : list) {
      resultMsg.append("------------------\n");
      resultMsg.append("服务器: ").append(role.getRegion_name()).append("\n");
      resultMsg.append("uid: ").append(role.getGame_uid()).append("\n");
      resultMsg.append("角色名称: ").append(role.getNickname()).append("\n");
      resultMsg.append("角色等级: ").append(role.getLevel()).append("\n");
    }
    resultMsg.append("------------------");
    return resultMsg.toString();
  }






	/*@Override
	public String bbsSign(MiHoYoEntity miHoYoEntity) throws IOException {
		Headers headers = OkHttpUtils.addHeaders(headerMap(miHoYoEntity));
		JSONObject signJsonObject = OkHttpUtils.postJson("https://bbs-api.mihoyo.com/apihub/sapi/signIn",
				OkHttpUtils.addJson("{\"gids\":\"2\"}"), headers);
		if (signJsonObject.getInteger("retcode") == 0){
			return "讨论区签到成功；";
		}else return "讨论区签到失败，" + signJsonObject.getString("message");
	}
	@Override
	public String bbsPost(MiHoYoEntity miHoYoEntity, String id) throws IOException {
		JSONObject postJsonObject = OkHttpUtils.getJson("https://bbs-api.mihoyo.com/post/api/getPostFull?post_id=" + id,
				OkHttpUtils.addHeaders(headerMap(miHoYoEntity)));
		if (postJsonObject.getInteger("retcode") == 0){
			return "浏览帖子成功；";
		}else return "浏览帖子失败，" + postJsonObject.getString("message");
	}
	@Override
	public String bbsLike(MiHoYoEntity miHoYoEntity, String id) throws IOException {
		JSONObject jsonObject = OkHttpUtils.postJson("https://bbs-api.mihoyo.com/apihub/sapi/upvotePost",
				OkHttpUtils.addJson("{\"is_cancel\":false,\"post_id\":\"" + id + "\"}"),
				OkHttpUtils.addHeaders(headerMap(miHoYoEntity)));
		if (jsonObject.getInteger("retcode") == 0){
			return "点赞帖子成功；";
		}else return "点赞帖子失败，" + jsonObject.getString("message");
	}
	@Override
	public String bbsShare(MiHoYoEntity miHoYoEntity, String id) throws IOException {
		JSONObject jsonObject = OkHttpUtils.getJson("https://bbs-api.mihoyo.com/apihub/api/getShareConf?entity_id=" + id
						+ "&entity_type=1",
				OkHttpUtils.addHeaders(headerMap(miHoYoEntity)));
		if (jsonObject.getInteger("retcode") == 0){
			return "分享帖子成功；";
		}else return "分享帖子失败，" + jsonObject.getString("message");
	}*/

  private static String ds2(String url, String body) {
    String t = String.valueOf(System.currentTimeMillis() / 1000);
    String r = CommonUtils.random(6);
    String query = "";
    String[] split = url.split("\\?");
    if (split.length == 2) {
      String[] params = split[1].split("&");
      Arrays.sort(params);
      query = StringUtils.join(params, "&");
    }
    String concat = "salt=" + "xV8v4Qu54lUKrEYFZkJhB8cuOh9Asafs" + "&t=" + t + "&r=" + r + "&b=" + body + "&q=" + query;
    String c = DigestUtils.md5DigestAsHex(concat.getBytes(StandardCharsets.UTF_8));
    return t + "," + r + "," + c;
  }

  /**
   * 账号信息请求头
   *
   * @date 2021-07-28 11:13
   */
  private static Header[] getCharacterHeaders(String url, String cookie, String body) {
    Header[] headers = new Header[5];
    headers[0] = new BasicHeader("Cookie", cookie);
    headers[1] = new BasicHeader("x-rpc-client_type", "5");
    headers[2] = new BasicHeader("x-rpc-app_version", "2.11.1");
    headers[3] = new BasicHeader("user-agent", UA.GEN_SHIN_INFO2.getValue());
    headers[4] = new BasicHeader("DS", ds2(url, body));
    return headers;
  }

  /**
   * 获取账号信息
   *
   * @date 2021-07-28 11:25
   */
  public static GenshinInfo genShinUserInfo(String gameId, String channel) {
    GenshinInfo genshinInfo = null;
    // cn_gf01 官服(天空岛)  cn_qd01 B服(世界树)
    try {
      String url = APIConst.GEN_SHIN_INFO.replace("CHANNEL", channel) + gameId;
      String infoStr = ServletUtils.get(url, getCharacterHeaders(url, COOKIE, ""));
      MiHoYoResult miHoYoResult = MapperUtils.deserialize(infoStr, MiHoYoResult.class);
      if (miHoYoResult == null || 0 != miHoYoResult.getRetcode()) {
        log.error("【MiHoYo】获取账号信息返回结果为空或请求异常, 请求返回数据原文: " + infoStr);
        throw new Exception("获取账号信息失败");
      }
      genshinInfo = MapperUtils.deserialize(MapperUtils.serialize(miHoYoResult.getData()), GenshinInfo.class);
    } catch (Exception e) {
      log.error("【MiHoYo】获取账号信息出现异常, 堆栈信息: ", e);
    }
    return genshinInfo;
  }

  /**
   * 获取角色信息
   *
   * @date 2021-08-07 10:00
   */
  public static List<GenshinCharacterInfo> getCharacterInfo(List<Long> characterIds, String gameId, String server) {
    List<GenshinCharacterInfo> resultList = new ArrayList<>();
    try {
      String data = MapperUtils.serialize(new GenshinCharacterReq(characterIds, gameId, server));
      String info = ServletUtils.post(APIConst.GEN_SHIN_CHARACTER_INFO, data,
          getCharacterHeaders(APIConst.GEN_SHIN_CHARACTER_INFO, COOKIE, data));
      MiHoYoResult miHoYoResult = MapperUtils.deserialize(info, MiHoYoResult.class);
      if (null == miHoYoResult) {
        return resultList;
      }
      if (miHoYoResult.getRetcode() == 0 && ProjectConst.OK.equals(miHoYoResult.getMessage())) {
        Map<String, Object> map = (Map<String, Object>) miHoYoResult.getData();
        resultList = MapperUtils.deserialize(MapperUtils.serialize(map.get("avatars")), new TypeReference<>() {
        });
      }
    } catch (Exception e) {
      log.error("【MiHoYo】获取人物信息出现异常, 堆栈信息: ", e);
    }
    return resultList;
  }

  /**
   * 获取深境螺旋记录(本期、上期)
   *
   * @date 2021-08-07 16:09
   */
  public static String getSpiralAbyss(String gameId) {
    StringBuilder builder = new StringBuilder("上期深境螺旋：");
    try {
      // 上期
      String preUrl = APIConst.GEN_SHIN_ABYSS_PREVIOUS + gameId;
//      log.info("上期深境螺旋记录: {}", ServletUtils.get(preUrl, getCharacterHeaders(preUrl, COOKIE, "")));
      MiHoYoResult result = MapperUtils.deserialize(ServletUtils.get(preUrl, getCharacterHeaders(preUrl, COOKIE, "")), MiHoYoResult.class);
      GenshinSpiralAbyss abyss;
      if (null == result) {
        builder.append("查询失败！");
      } else {
        abyss = MapperUtils.deserialize(MapperUtils.serialize(result.getData()), GenshinSpiralAbyss.class);
        ConvertUtils.handleAbyssRecord(builder, abyss);
      }
      // 本期
      String thisUrl = APIConst.GEN_SHIN_ABYSS_THIS + gameId;
//      log.info("本期深境螺旋记录: {}", ServletUtils.get(thisUrl, getCharacterHeaders(thisUrl, COOKIE, "")));
      result = MapperUtils.deserialize(ServletUtils.get(thisUrl, getCharacterHeaders(thisUrl, COOKIE, "")), MiHoYoResult.class);
      if (null == result) {
        builder.append("查询失败！\n");
      } else {
        abyss = MapperUtils.deserialize(MapperUtils.serialize(result.getData()), GenshinSpiralAbyss.class);
        builder.append("\n本期深境螺旋：");
        ConvertUtils.handleAbyssRecord(builder, abyss);
      }
      return builder.substring(0, builder.length() - 1);
    } catch (Exception e) {
      log.error("【MiHoYo】获取深境螺旋记录失败, 堆栈信息: ", e);
    }
    return "深境螺旋数据获取失败！";
  }

}
