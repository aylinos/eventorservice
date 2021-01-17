package com.eventor.response;

import com.eventor.payload.response.MessageResponse;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MessageResponseTest {
    String message1 = "User not found";
    String message2 = "Event not found";

    MessageResponse messageResponse = new MessageResponse(message1);

    @Test
    public void newResponse_constructor()
    {
        assertNotNull(messageResponse);
        assertNotNull(messageResponse.getMessage());
    }

    @Test
    public void setMessage()
    {
        messageResponse.setMessage(message2);
        assertEquals(message2, messageResponse.getMessage());
    }

    @Test
    public void getMessage()
    {
        assertEquals(message1, messageResponse.getMessage());
    }
}
