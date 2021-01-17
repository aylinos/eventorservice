package com.eventor.response;

import com.eventor.model.Event;
import com.eventor.model.User;
import com.eventor.payload.response.ResponseWithError;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseWithErrorTest {
    User creator = new User("gray@gmail.com", "grayb", "a12347");

    Event event1 = new Event(creator, "Java workshop", "Level: Beginners", true, false);
    Event event2 = new Event(creator, "Dance party", "Music: 00s", true, false);

    String error = "Error test";

    ResponseWithError<Event> responseWithError = new ResponseWithError<>(event1, null);

    @Test
    public void newResponse_constructor()
    {
        assertNotNull(responseWithError);
        assertNotNull(responseWithError.getItem());
    }

    @Test
    public void setItem()
    {
        responseWithError.setItem(event2);
        assertEquals(event2, responseWithError.getItem());
    }

    @Test
    public void getItem()
    {
        Event item = responseWithError.getItem();
        assertEquals(event1, item);
    }

    @Test
    public void setError()
    {
        responseWithError.setError(error);
        assertEquals(error, responseWithError.getError());
    }

    @Test
    public void getError()
    {
        assertEquals(null, responseWithError.getError());
    }
}
