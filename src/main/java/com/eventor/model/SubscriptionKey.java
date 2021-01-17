package com.eventor.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class SubscriptionKey implements Serializable {
    @Column(name = "user_id")
    int userId;

    @Column(name = "event_id")
    long eventId;

    public SubscriptionKey(int user, long event)
    {
        this.userId = user;
        this.eventId = event;
    }
}
