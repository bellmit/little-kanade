package com.plumekanade.robot.config;

import com.plumekanade.robot.constants.AuthConst;
import com.plumekanade.robot.interceptor.AuthInterceptor;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web拦截配置类
 * @version 1.0
 * @author kanade
 * @date 2022-01-28 12:22
 */
@Configuration
@AllArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private final AuthInterceptor authInterceptor;

  @Override
  public void addInterceptors(@NotNull InterceptorRegistry registry) {
    InterceptorRegistration registration = registry.addInterceptor(authInterceptor);
    registration.addPathPatterns("/**");
    registration.excludePathPatterns(AuthConst.EXCLUDE_PATH);
  }

}
