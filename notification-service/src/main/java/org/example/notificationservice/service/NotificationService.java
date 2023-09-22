package org.example.notificationservice.service;

import org.example.notificationservice.model.Notification;
import org.example.notificationservice.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {
    @Autowired
    NotificationRepository notificationRepository;
    public void saveNotification(String message){
        Notification notification = Notification.builder()
                .message(message)
                .time(LocalDateTime.now())
                .build();
        notificationRepository.save(notification);
    }
    public void deleteNotification(String id){
        notificationRepository.deleteById(id);
    }
}
