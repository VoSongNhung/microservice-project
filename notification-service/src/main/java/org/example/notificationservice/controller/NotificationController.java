package org.example.notificationservice.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@EnableKafka
@RequestMapping("/api/notification")
public class NotificationController {
    @Autowired
    NotificationService notificationService;
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteNotification(@PathVariable String id){
        notificationService.deleteNotification(id);
        return "Notification with id "+id+" is deleted";
    }
    @KafkaListener(topics = "notificationTopic")
    public void handleNotification(String numberOrder){
        String mess = "Received notification order with id "+ numberOrder;
        notificationService.saveNotification(mess);
        log.info(mess);
    }
}
