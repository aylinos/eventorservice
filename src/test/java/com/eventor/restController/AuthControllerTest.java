package com.eventor.restController;

import com.eventor.controller.AuthController;
import com.eventor.controller.SubscriptionController;
import com.eventor.model.Role;
import com.eventor.model.User;
import com.eventor.payload.request.LogInRequest;
import com.eventor.payload.request.SignUpRequest;
import com.eventor.payload.response.MessageResponse;
import com.eventor.repository.EventRepository;
import com.eventor.repository.RoleRepository;
import com.eventor.repository.SubscriptionRepository;
import com.eventor.repository.UserRepository;
import com.eventor.security.jwt.JwtUtils;
import com.eventor.security.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
public class AuthControllerTest {
    @MockBean
    MockCustomUserSecurityContextFactory mck;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthenticationManager authenticationManager;

    @MockBean
    EventRepository eventRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    RoleRepository roleRepository;

    @MockBean
    SubscriptionRepository subscriptionRepository;

    @MockBean
    UserDetailsServiceImpl userDetailsService;

    @MockBean
    JwtUtils jwtUtils;

    @MockBean
    PasswordEncoder encoder;

    @MockBean
    SubscriptionController subscriptionController;

    LogInRequest logInRequest = new LogInRequest();
    SignUpRequest signUpRequest = new SignUpRequest();

    @MockCustomUser(username = "rayb")
    @Test
    void testAuthenticateUser_SignIn_ValidLogInRequestProvided() throws Exception
    {
        //  Arrange
        logInRequest.setUsername("rayb");
        logInRequest.setPassword("a12347");
        String url = "/api/auth/signin/";

        //  Act
        Mockito.when(userRepository.existsByUsername(logInRequest.getUsername())).thenReturn(true);
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInRequest.getUsername(), logInRequest.getPassword()))).thenReturn(SecurityContextHolder.getContext().getAuthentication());

        //  Assert status response
        MvcResult mvcResult = mockMvc.perform(
                post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(logInRequest)))
                        .andExpect(status().isOk())
                        .andReturn();
    }

    @MockCustomUser(username = "rayb")
    @Test
    void testAuthenticateUser_FailToSignIn_InvalidUsernameProvided() throws Exception
    {
        //  Arrange
        logInRequest.setUsername("rayb");
        logInRequest.setPassword("a12347");

        String url = "/api/auth/signin/";

        //  Act
        Mockito.when(userRepository.existsByUsername(logInRequest.getUsername())).thenReturn(false);

        //  Assert status response
        MvcResult mvcResult = mockMvc.perform(
                post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(logInRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        MessageResponse messageResponse = new MessageResponse("Error: Username does not exist!");

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse = objectMapper.writeValueAsString(messageResponse);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
    }

//    @MockCustomUser(username = "rayb")
//    @Test
//    void testAuthenticateUser_FailToSignIn_InvalidPasswordProvided() throws Exception
//    {
//        //  Arrange
//        logInRequest.setUsername("rayb");
//        logInRequest.setPassword("a1234");
//
//        String url = "/api/auth/signin/";
//
//        //  Act
//        Mockito.when(userRepository.existsByUsername(logInRequest.getUsername())).thenReturn(true);
//        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(logInRequest.getUsername(), logInRequest.getPassword()))).thenReturn(SecurityContextHolder.getContext().getAuthentication());
//
//        //  Assert status response
//        MvcResult mvcResult = mockMvc.perform(
//                post(url)
//                        .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(logInRequest)))
//                .andExpect(status().isOk())
//                .andReturn();
//    }

    @Test
    void testAuthenticateUser_SignUp_ValidSignUpRequestProvided() throws Exception
    {
        //  Arrange
        signUpRequest.setEmail("rayb@mail.com");
        signUpRequest.setUsername("rayb");
        signUpRequest.setPassword("a12347");

        Role userRole = new Role("user");
        User newUser = new User(signUpRequest.getEmail(), signUpRequest.getUsername(), signUpRequest.getPassword());

        MessageResponse response = new MessageResponse("User registered successfully!");

        String url = "/api/auth/signup";

        //  Act
        Mockito.when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        Mockito.when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

        //  Assert status response
        MvcResult mvcResult = mockMvc.perform(
                post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                        .andExpect(status().isOk())
                        .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    @Test
    void testAuthenticateUser_FailToSignUp_ValidSignUpRequestProvided_EmailNotUnique() throws Exception
    {
        //  Arrange
        signUpRequest.setEmail("rayb@mail.com");
        signUpRequest.setUsername("rayb");
        signUpRequest.setPassword("a12347");

        User newUser = new User(signUpRequest.getEmail(), signUpRequest.getUsername(), signUpRequest.getPassword());

        MessageResponse response = new MessageResponse("Error: Email is already in use!");

        String url = "/api/auth/signup";

        //  Act
        Mockito.when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(true);

        //  Assert status response
        MvcResult mvcResult = mockMvc.perform(
                post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    @Test
    void testAuthenticateUser_FailToSignUp_ValidSignUpRequestProvided_UsernameNotUnique() throws Exception
    {
        //  Arrange
        signUpRequest.setEmail("rayb@mail.com");
        signUpRequest.setUsername("rayb");
        signUpRequest.setPassword("a12347");

        User newUser = new User(signUpRequest.getEmail(), signUpRequest.getUsername(), signUpRequest.getPassword());

        MessageResponse response = new MessageResponse("Error: Username is already taken!");

        String url = "/api/auth/signup";

        //  Act
        Mockito.when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        Mockito.when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(true);

        //  Assert status response
        MvcResult mvcResult = mockMvc.perform(
                post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isBadRequest())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    @Test
    void testAuthenticateUser_SignUp_ValidSignUpRequestProvided_WithTwoRoles() throws Exception
    {
        //  Arrange
        signUpRequest.setEmail("rayb@mail.com");
        signUpRequest.setUsername("rayb");
        signUpRequest.setPassword("a12347");

        Role userRole = new Role("user");
        Role adminRole = new Role("admin");
        Set<String> role = new HashSet<>();
        role.add(userRole.getName());
        role.add(adminRole.getName());

        signUpRequest.setRole(role);

        User newUser = new User(signUpRequest.getEmail(), signUpRequest.getUsername(), signUpRequest.getPassword());

        MessageResponse response = new MessageResponse("User registered successfully!");

        String url = "/api/auth/signup";

        //  Act
        Mockito.when(userRepository.existsByEmail(signUpRequest.getEmail())).thenReturn(false);
        Mockito.when(userRepository.existsByUsername(signUpRequest.getUsername())).thenReturn(false);
        Mockito.when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        Mockito.when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        Mockito.when(userRepository.save(newUser)).thenReturn(newUser);

        //  Assert status response
        MvcResult mvcResult = mockMvc.perform(
                post(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isOk())
                .andReturn();

        String actualJsonResponse = mvcResult.getResponse().getContentAsString();

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);

        Mockito.verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
        Mockito.verify(roleRepository, times(1)).findByName("ROLE_USER");
    }
}
