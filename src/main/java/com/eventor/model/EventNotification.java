package com.eventor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@NoArgsConstructor
@Table(name = "eventNotifications")
public class EventNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JsonIgnoreProperties("subscribedEvents")
    private User receiver;

    @ManyToOne
    @JsonIgnoreProperties("subscribedEvents")
    private User sender;

    @ManyToOne
    @JsonIgnoreProperties("subscriptions")
    private Event event;

    private boolean opened;

    private boolean hasSubscribed;

    private Date created;

    @PrePersist
    protected void onCreate() {
        created = new Date();
    }

    public EventNotification(User receiver, User sender, Event event, boolean hasSubscribed)
    {
        this.receiver = receiver;
        this.sender = sender;
        this.event = event;
        this.opened = false;
        this.hasSubscribed = hasSubscribed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isHasSubscribed() {
        return hasSubscribed;
    }

    public void setHasSubscribed(boolean hasSubscribed) {
        this.hasSubscribed = hasSubscribed;
    }

    public Date getCreated() {
        return created;
    }
}
