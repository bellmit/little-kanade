package com.plumekanade.robot;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableScheduling
@SpringBootApplication
@MapperScan("com.plumekanade.robot.mapper")    //设置mapper接口扫描地址
@EnableTransactionManagement(proxyTargetClass = true)   //开启事务管理 因为不写Service接口层 所以声明基于类代理(CGlib)
public class LittleKanadeApplication {

  public static void main(String[] args) {
    SpringApplication.run(LittleKanadeApplication.class, args);
  }

  /**
   * Mybatis-Plus分页插件
   */
  @Bean
  public MybatisPlusInterceptor paginationInterceptor() {
    MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
    interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
    return interceptor;
  }

}
