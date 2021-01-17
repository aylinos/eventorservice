package com.eventor.model;

import org.junit.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubscriptionKeyTest {

    int userId = 1;
    long eventId = 1;

    SubscriptionKey subscriptionKey = new SubscriptionKey(userId, eventId);

    @Test
    public void newSubscriptionKey_constructor()
    {
        assertNotNull(subscriptionKey);
    }

    @Test
    public void setUserId()
    {
        subscriptionKey.userId = 7;
        assertEquals(7, subscriptionKey.userId);
    }

    @Test
    public void getUserId()
    {
        assertEquals(1, subscriptionKey.userId);
    }

    @Test
    public void setEventId()
    {
        subscriptionKey.eventId = 7;
        assertEquals(7, subscriptionKey.eventId);
    }

    @Test
    public void getEventId()
    {
        assertEquals(1, subscriptionKey.eventId);
    }
}
