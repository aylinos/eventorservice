package com.eventor.request;

import com.eventor.payload.request.LogInRequest;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LogInRequestTest {
    String username = "rayb";
    String password = "a12347";

    LogInRequest logInRequest = new LogInRequest();

    @Test
    public void setUsername()
    {
        logInRequest.setUsername(username);
        assertEquals(username, logInRequest.getUsername());
    }

    @Test
    public void getUsername()
    {
        logInRequest.setUsername(username);
        String currentUsername = logInRequest.getUsername();
        assertEquals(username, currentUsername);
    }

    @Test
    public void setPassword()
    {
        logInRequest.setPassword(password);
        assertEquals(password, logInRequest.getPassword());
    }

    @Test
    public void getPassword()
    {
        logInRequest.setPassword(password);
        String currentPassword = logInRequest.getPassword();
        assertEquals(password, currentPassword);
    }
}
