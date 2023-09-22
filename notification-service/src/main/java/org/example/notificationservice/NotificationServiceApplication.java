package org.example.notificationservice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;

@SpringBootApplication
@EnableKafka
@EnableDiscoveryClient
@Slf4j
public class NotificationServiceApplication {
    public static void main(String[] args) {

        SpringApplication.run(NotificationServiceApplication.class,args);
    }


}