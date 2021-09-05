package com.scaudachuang.catlife.web;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author hiluyx
 * @since 2021/9/1 11:17
 **/
@SpringBootApplication
@MapperScan(basePackages = "com.scaudachuang.catlife.web.dao")
public class CatlifeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatlifeApplication.class, args);
    }

}
