package com.eventor.websockets;

import com.eventor.model.Event;
import com.eventor.model.User;

public class NewSubscriberMessage {
    private User subscriber;
    private Event myEvent;

    public NewSubscriberMessage() {
    }

    public NewSubscriberMessage(User subscriber, Event myEvent) {
        this.subscriber = subscriber;
        this.myEvent = myEvent;
    }

    public User getSubscriber() {
        return subscriber;
    }

    public void setSubscriber(User subscriber) {
        this.subscriber = subscriber;
    }

    public Event getMyEvent() {
        return myEvent;
    }

    public void setMyEvent(Event myEvent) {
        this.myEvent = myEvent;
    }
}
