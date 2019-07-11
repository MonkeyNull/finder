package com.monkey.finder.find;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@MapperScan({"com.monkey.finder.find.dao"})
public class FindApplication {

    public static void main(String[] args) {
        SpringApplication.run(FindApplication.class, args);
    }

}

