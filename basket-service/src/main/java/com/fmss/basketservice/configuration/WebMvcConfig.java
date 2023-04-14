package com.fmss.basketservice.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  @Autowired
  private CorsFilter corsFilter;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(corsFilter).addPathPatterns("/**");
  }
}
