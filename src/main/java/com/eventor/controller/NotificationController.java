package com.eventor.controller;

import com.eventor.model.Event;
import com.eventor.model.EventNotification;
import com.eventor.model.User;
import com.eventor.payload.response.ResponseWithError;
import com.eventor.repository.EventNotificationRepository;
import com.eventor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/eventor")
public class NotificationController {
    @Autowired
    EventNotificationRepository eventNotificationRepository;

    @Autowired
    UserRepository userRepository;

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/notifications")
    public ResponseEntity<ResponseWithError<List<EventNotification>>> getMyNotifications(@RequestParam("receiver") int receiverID) {
        Optional<User> receiverOpt;
        try {
            receiverOpt = userRepository.findById(receiverID);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "User repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        }
        if (!receiverOpt.isPresent()) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "User not found"), HttpStatus.NOT_FOUND);
        }

        User receiver = receiverOpt.get();
        List<EventNotification> notifications = new ArrayList<>();

        try {
            eventNotificationRepository.findByReceiver(receiver).forEach(notifications::add);
        } catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "Event notification repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        }

        if (notifications.isEmpty()) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "No notifications found"), HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(new ResponseWithError<>(notifications, null), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PutMapping("/notifications/updateStatus")
    public ResponseEntity<ResponseWithError<Boolean>> changeNotificationsStatus(@RequestBody List<EventNotification> notifications)  {
        try {
            for (EventNotification n : notifications) {
                if (eventNotificationRepository.existsById(n.getId())) {
                    n.setOpened(true);
                    eventNotificationRepository.save(n);
                }
            }
        }catch (DataAccessException e) {
            return new ResponseEntity<>(new ResponseWithError<>(null, "Event notification repository not responding"), HttpStatus.SERVICE_UNAVAILABLE);
        }
        return new ResponseEntity<>(new ResponseWithError<>(true, null), HttpStatus.OK);
    }
}
