package com.eventor.repository;

import com.eventor.model.EventNotification;
import com.eventor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventNotificationRepository extends JpaRepository<EventNotification, Long> {
    List<EventNotification> findByReceiver(User receiver);
}
