package com.plumekanade.robot.filter;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.plumekanade.robot.constants.AuthConst;
import com.plumekanade.robot.utils.RedisCertUtils;
import com.plumekanade.robot.utils.ServletUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录判断Filter
 * 个人用没必要嵌入权限
 *
 * @author kanade
 * @date 2021-01-12 17:31
 */
@Slf4j
@Configuration
@AllArgsConstructor
public class AuthFilter extends OncePerRequestFilter {

  private final RedisCertUtils redisCertUtils;

  @Override
  protected void doFilterInternal(HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain)
      throws ServletException, IOException {

    String token = request.getHeader(AuthConst.TOKEN_HEADER);
    if (StringUtils.isBlank(token)) {   // token为空 跳登录
      ServletUtils.render(response, "用户未登录");
    }
    chain.doFilter(request, response);
  }
}
