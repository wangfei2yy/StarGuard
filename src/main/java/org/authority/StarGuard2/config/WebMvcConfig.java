package org.authority.StarGuard2.config;

import org.authority.StarGuard2.config.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC配置类，用于配置静态资源的访问路径和拦截器
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    // 注入登录拦截器
    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置静态资源的访问路径
        // 注意：由于应用配置了server.servlet.context-path=/star-guard
        // Spring Boot会自动将这个前缀添加到所有URL中
        // 所以这里只需要配置/static/**，实际访问路径会变成/star-guard/static/**
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 添加登录拦截器，拦截所有请求
        // 注意：在拦截器内部已处理了公开资源的过滤，所以这里不需要再配置excludePathPatterns
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**");
    }
}