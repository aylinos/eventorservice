package com.eventor.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Subscription {
    @EmbeddedId
    SubscriptionKey id;

    @ManyToOne
    @MapsId("eventId")
    @JoinColumn(name = "event_id")
    Event event;

    public User getUser() {
        return user;
    }

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @Column(name = "subscription_date")
    Date subscriptionDate;

    @Column(name = "unsubscription_date")
    Date unsubscriptionDate;

    @Column(name = "unsubscribed", columnDefinition = "boolean default false")
    Boolean unsubscribed;

    public Subscription(Event event, User user, Date subscriptionDate)
    {
        this.id = new SubscriptionKey(user.getId(), event.getId());
        this.event = event;
        this.user = user;
        this.subscriptionDate = subscriptionDate;
        this.setUnsubscribed(false);
    }

    public Event getEvent() {
        return event;
    }

    public Date getSubscriptionDate() {
        return subscriptionDate;
    }

    public void setSubscriptionDate(Date subscriptionDate) {
        this.subscriptionDate = subscriptionDate;
    }

    public Date getUnsubscriptionDate() {
        return unsubscriptionDate;
    }

    public void setUnsubscriptionDate(Date unsubscriptionDate) {
        this.unsubscriptionDate = unsubscriptionDate;
    }

    public Boolean getUnsubscribed() {
        return unsubscribed;
    }

    public void setUnsubscribed(Boolean unsubscribed) {
        this.unsubscribed = unsubscribed;
    }
}
