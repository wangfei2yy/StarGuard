package org.authority.StarGuard2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web配置类，用于配置Web相关的设置
 * 
 * @author System
 * @version 1.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 配置CORS（跨域资源共享）
     * 
     * @param registry CORS注册表
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // 允许/api/**路径的跨域访问
        registry.addMapping("/api/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
        
        // 允许/star-guard-mcp路径的跨域访问，用于MCP工具集成
        registry.addMapping("/star-guard-mcp/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}