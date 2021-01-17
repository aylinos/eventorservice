package com.eventor.model;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubscriptionTest {

    User user = new User("grayb@gmail.com", "grayb", "a12347");
    Event event = new Event(1, user, "Backyard Party", "Bring drinks!", false, true);

    String subscriptionDateString = "2020-12-25";
    DateFormat df = new SimpleDateFormat("YYYY-MM-dd");

    @Test
    void newSubscription_constructor() throws Exception
    {
        Date subscriptionDate = df.parse(subscriptionDateString);

        Subscription newSubscription = new Subscription(event, user, subscriptionDate);
        assertNotNull(user);
    }

    @Test
    void getEvent() throws Exception
    {
        Date subscriptionDate = df.parse(subscriptionDateString);

        Subscription newSubscription = new Subscription(event, user, subscriptionDate);
        assertEquals(event, newSubscription.getEvent());
    }

    @Test
    void getSubscriptionDate() throws Exception
    {
        Date subscriptionDate = df.parse(subscriptionDateString);

        Subscription newSubscription = new Subscription(event, user, subscriptionDate);
        assertEquals(subscriptionDate, newSubscription.getSubscriptionDate());
    }
}
