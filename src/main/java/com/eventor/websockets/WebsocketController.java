package com.eventor.websockets;

import com.eventor.model.EventNotification;
import com.eventor.repository.EventNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {
    @Autowired
    EventNotificationRepository eventNotificationRepository;

    @MessageMapping("/new")
    @SendTo("/notifications/subscribers")
    public EventNotification newSubscriber(NewSubscriberMessage content) throws Exception {
//        Thread.sleep(1000); // simulated delay
        EventNotification newNotification = new EventNotification(content.getMyEvent().getCreator(), content.getSubscriber(), content.getMyEvent(), true);
        eventNotificationRepository.save(newNotification);
        return newNotification;
    }
}
