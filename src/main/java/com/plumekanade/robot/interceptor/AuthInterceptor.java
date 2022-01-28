package com.plumekanade.robot.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.plumekanade.robot.constants.AuthConst;
import com.plumekanade.robot.enums.CodeEnum;
import com.plumekanade.robot.utils.RedisCertUtils;
import com.plumekanade.robot.utils.ServletUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 授权拦截器
 * @version 1.0
 * @author kanade
 * @date 2022-01-28 12:17
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

  private final RedisCertUtils redisCertUtils;


  @Override
  public boolean preHandle(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull Object handler) throws Exception {

    String token = request.getHeader(AuthConst.TOKEN_HEADER);
    //          token为空                         非合法token
    if (StringUtils.isBlank(token) || !redisCertUtils.validToken(token)) {
      ServletUtils.render(response, CodeEnum.UN_LOGIN);   // 跳登录
      return false;
    }
    return true;
  }
}
