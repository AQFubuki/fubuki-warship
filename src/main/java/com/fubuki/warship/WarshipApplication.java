package com.fubuki.warship;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
@MapperScan(basePackages = "com.fubuki.warship.model.dao")
//@EnableSwagger2
public class WarshipApplication {

    public static void main(String[] args) {
        SpringApplication.run(WarshipApplication.class, args);
    }

}
