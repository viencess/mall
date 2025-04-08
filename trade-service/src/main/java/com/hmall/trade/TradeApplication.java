package com.hmall.trade;

import com.hmall.api.config.DefaultFeignConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "com.hmall.api.client", defaultConfiguration = DefaultFeignConfig.class)
@MapperScan("com.hmall.trade.mapper")
@SpringBootApplication
public class TradeApplication implements CommandLineRunner {

    @Value("${spring.rabbitmq.host}")
    private String rabbitHost;

    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    public static void main(String[] args) {
        SpringApplication.run(TradeApplication.class, args);
    }

    @Override
    public void run(String... args) {
        System.err.println("RabbitMQ Host: " + rabbitHost);
        System.err.println("Virtual Host: " + virtualHost);
    }
}