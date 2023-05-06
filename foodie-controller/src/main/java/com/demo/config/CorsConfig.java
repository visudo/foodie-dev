package com.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
public class CorsConfig {

    public CorsConfig() {

    }

    @Bean
    public CorsFilter corsFilter() {
        //1、添加cors配置信息
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://127.0.0.1:8000");
        //设置是否发送cookie信息
        config.setAllowCredentials(true);
        //设置允许请求的方式
        config.addAllowedMethod("*");
        //设置运行的header
        config.addAllowedHeader("*");
        //2、为url添加映射信息
        UrlBasedCorsConfigurationSource corsSource = new UrlBasedCorsConfigurationSource();
        corsSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(corsSource);
    }
}
