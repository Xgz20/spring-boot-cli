package com.xgz.cli.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 * @author: Xgz
 * @date: 2026/1/31
 */
@SpringBootApplication(scanBasePackages = "com.xgz.cli")
@MapperScan("com.xgz.cli.dao")
public class SpringBootCliApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootCliApplication.class, args);
    }
}
