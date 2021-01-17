package com.eventor.request;

import com.eventor.model.Role;
import com.eventor.payload.request.SignUpRequest;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SignUpRequestTest {
    String firstName = "Bill";
    String lastName = "Ray";
    String email = "bray@gmail.com";
    String username = "bray";
    String password = "a12347";
    Set<String> role = new HashSet<>();

    SignUpRequest signUpRequest = new SignUpRequest();

    @Test
    public void setFirstName()
    {
        signUpRequest.setFirstName(firstName);
        assertEquals(firstName, signUpRequest.getFirstName());
    }

    @Test
    public void getFirstName()
    {
        signUpRequest.setFirstName(firstName);
        String currentFirstName = signUpRequest.getFirstName();
        assertEquals(firstName, currentFirstName);
    }

    @Test
    public void setLastName()
    {
        signUpRequest.setLastName(lastName);
        assertEquals(lastName, signUpRequest.getLastName());
    }

    @Test
    public void getLastName()
    {
        signUpRequest.setLastName(lastName);
        String currentLastName = signUpRequest.getLastName();
        assertEquals(lastName, currentLastName);
    }

    @Test
    public void setEmail()
    {
        signUpRequest.setEmail(email);
        assertEquals(email, signUpRequest.getEmail());
    }

    @Test
    public void getEmail()
    {
        signUpRequest.setEmail(email);
        String currentEmail = signUpRequest.getEmail();
        assertEquals(email, currentEmail);
    }

    @Test
    public void setUsername()
    {
        signUpRequest.setUsername(username);
        assertEquals(username, signUpRequest.getUsername());
    }

    @Test
    public void getUsername()
    {
        signUpRequest.setUsername(username);
        String currentUsername = signUpRequest.getUsername();
        assertEquals(username, currentUsername);
    }

    @Test
    public void setPassword()
    {
        signUpRequest.setPassword(password);
        assertEquals(password, signUpRequest.getPassword());
    }

    @Test
    public void getPassword()
    {
        signUpRequest.setPassword(password);
        String currentPassword = signUpRequest.getPassword();
        assertEquals(password, currentPassword);
    }

    @Test
    public void setRole()
    {
        role.add("USER");
        signUpRequest.setRole(role);
        assertEquals(role, signUpRequest.getRole());
    }

    @Test
    public void getRole()
    {
        role.add("USER");
        signUpRequest.setRole(role);
        Set<String> currentRole = signUpRequest.getRole();
        assertEquals(role, signUpRequest.getRole());
        assertEquals(role, currentRole);
    }
}
