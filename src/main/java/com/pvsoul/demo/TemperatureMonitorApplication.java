package com.pvsoul.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@MapperScan("com.pvsoul.demo.mapper")
public class TemperatureMonitorApplication {


    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(TemperatureMonitorApplication.class, args);
        applicationContext.getBean(SocketServer.class).start();
    }
}

