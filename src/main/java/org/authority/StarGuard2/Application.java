package org.authority.StarGuard2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring Boot应用程序的主类
 * 作为应用程序的入口点
 */
@SpringBootApplication
public class Application {

    /**
     * 应用程序的入口点
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}