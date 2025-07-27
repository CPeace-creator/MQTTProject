package com.atguigu.mqtt;

import com.atguigu.mqtt.domain.MqttConfigurationProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(value = {MqttConfigurationProperties.class,MqttConfigurationProperties.class})
@MapperScan(basePackages = "com.atguigu.mqtt.mapper")
public class MqttClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(MqttClientApplication.class , args) ;
    }

}
