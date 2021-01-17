package com.eventor.restController;

import com.eventor.controller.SubscriptionController;
import com.eventor.exception.ResourceNotFound;
import com.eventor.model.Event;
import com.eventor.model.Subscription;
import com.eventor.model.User;
import com.eventor.payload.response.MessageResponse;
import com.eventor.repository.EventRepository;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SubscriptionController.class)
public class SubscriptionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    SubscriptionRepository subscriptionRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    User user = new User("grayb@mail.com", "grayb", "a12347");
    Event event1 = new Event(1, user, "Backyard Party", "Bring drinks!", false, true);
    Event event2 = new Event(2, user, "Chess tournament", "Participants aged between 10-80 years old.", true, false);
    Event eventInBody = event1;
    Event foundEvent = event1;

    String subscriptionDate = "2020-12-25";
    DateFormat df = new SimpleDateFormat("YYYY-MM-dd");

    @WithMockUser(roles = "USER")
    @Test
    void testSubscribeToEvent_Subscribe_ValidUserProvided_ValidEventProvided() throws Exception {
        //        Arrange
        int userId = 1;
        Set<Subscription> subscriptions = new HashSet<>();
        foundEvent.setSubscriptions(subscriptions);
        eventInBody.setLastSubscription(df.parse(subscriptionDate));
        Subscription newSubscription = new Subscription(foundEvent, user, eventInBody.getLastSubscription());
        MessageResponse response = new MessageResponse("You subscribed successfully!");

        String url = "/eventor/subscriptions/subscribe/" + userId;

        //        Act
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(eventRepository.findById(eventInBody.getId())).thenReturn(Optional.of(foundEvent));
        Mockito.when(eventRepository.save(foundEvent)).thenReturn(foundEvent);
        Mockito.when(subscriptionRepository.save(newSubscription)).thenReturn(newSubscription);

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventInBody)))
                        .andExpect(status().isOk())
                        .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
        Mockito.verify(userRepository, times(1)).findById(userId);
        Mockito.verify(eventRepository, times(1)).findById(eventInBody.getId());
    }

    @WithMockUser(roles = "USER")
    @Test
    void testSubscribeToEvent_FailsToSubscribe_InvalidUserProvided_ValidEventProvided() throws Exception {
        //        Arrange
        int userId = 1;
        MessageResponse response = new MessageResponse("User not found!");

        String url = "/eventor/subscriptions/subscribe/" + userId;

        //        Act
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventInBody)))
                        .andExpect(status().isNotFound())
                        .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
        Mockito.verify(userRepository, times(1)).findById(userId);
        Mockito.verify(eventRepository, times(0)).findById(eventInBody.getId());
    }

    @WithMockUser(roles = "USER")
    @Test
    void testSubscribeToEvent_FailsToSubscribe_ValidUserProvided_InvalidEventProvided() throws Exception {
        //        Arrange
        int userId = 1;
        MessageResponse response = new MessageResponse("Event not found!");

        String url = "/eventor/subscriptions/subscribe/" + userId;

        //        Act
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(eventRepository.findById(eventInBody.getId())).thenReturn(Optional.empty());

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventInBody)))
                .andExpect(status().isNotFound())
                .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
        Mockito.verify(userRepository, times(1)).findById(userId);
        Mockito.verify(eventRepository, times(1)).findById(eventInBody.getId());
    }

    @WithMockUser(roles = "USER")
    @Test
    void testGetSubscriptionsForUser_GetSubscriptions_ValidUserProvided() throws Exception {
        //        Arrange
        int userId = 1;

        Set<Subscription> subscriptions = new HashSet<>();
        Subscription subscription1 = new Subscription(event1, user, df.parse(subscriptionDate));
        Subscription subscription2 = new Subscription(event2, user, df.parse(subscriptionDate));

        subscriptions.add(subscription1);
        subscriptions.add(subscription2);

        user.setSubscriptions(subscriptions);

        Set<Event> events = new HashSet<>();
        events.add(event1);
        events.add(event2);

        String url = "/eventor/subscriptions/" + userId;

        //        Act
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                get(url))
                .andExpect(status().isOk())
                .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(events);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
        Mockito.verify(userRepository, times(1)).findById(userId);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testGetSubscriptionsForUser_Get0Subscriptions_ValidUserProvided() throws Exception {
        //        Arrange
        int userId = 1;

        Set<Subscription> subscriptions = new HashSet<>();
        user.setSubscriptions(subscriptions);

        Set<Event> events = new HashSet<>();

        String url = "/eventor/subscriptions/" + userId;

        //        Act
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                get(url))
                .andExpect(status().isNoContent())
                .andReturn();

        //        Assert
        Mockito.verify(userRepository, times(1)).findById(userId);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testGetSubscriptionsForUser_Fail_InvalidUserProvided() throws Exception {
        //        Arrange
        int userId = 1;

        String url = "/eventor/subscriptions/" + userId;

        //        Act
        Mockito.when(userRepository.findById(userId)).thenThrow(new ResourceNotFound(SubscriptionController.USER_NOT_FOUND_EXCEPTION_CONTENT + userId));

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                get(url))
                .andExpect(status().isNotFound())
                .andReturn();

        //        Assert
        Mockito.verify(userRepository, times(1)).findById(userId);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUnsubscribeToEvent_Unsubscribe_ValidUserProvided_ValidEventProvided() throws Exception {
        //        Arrange
        int userId = 1;

        Set<Subscription> subscriptions = new HashSet<>();
        Subscription subscription1 = new Subscription(event1, user, df.parse(subscriptionDate));
        Subscription subscription2 = new Subscription(event2, user, df.parse(subscriptionDate));

        subscriptions.add(subscription1);
        subscriptions.add(subscription2);

        user.setSubscriptions(subscriptions);

        eventInBody.setLastSubscription(df.parse(subscriptionDate));
        Subscription newSubscription = new Subscription(foundEvent, user, eventInBody.getLastSubscription());
        MessageResponse response = new MessageResponse("You unsubscribed successfully!");

        String url = "/eventor/subscriptions/unsubscribe/" + userId;

        //        Act
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(eventRepository.findById(eventInBody.getId())).thenReturn(Optional.of(foundEvent));
        Mockito.when(eventRepository.save(foundEvent)).thenReturn(foundEvent);
        Mockito.when(subscriptionRepository.save(newSubscription)).thenReturn(newSubscription);

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventInBody)))
                .andExpect(status().isOk())
                .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
        Mockito.verify(userRepository, times(1)).findById(userId);
        Mockito.verify(eventRepository, times(1)).findById(eventInBody.getId());
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUnsubscribeToEvent_FailsToUnsubscribe_InvalidUserProvided_ValidEventProvided() throws Exception {
        //        Arrange
        int userId = 1;
        MessageResponse response = new MessageResponse("User not found!");

        String url = "/eventor/subscriptions/unsubscribe/" + userId;

        //        Act
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventInBody)))
                .andExpect(status().isNotFound())
                .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
        Mockito.verify(userRepository, times(1)).findById(userId);
        Mockito.verify(eventRepository, times(0)).findById(eventInBody.getId());
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUnsubscribeToEvent_FailsToUnsubscribe_ValidUserProvided_InvalidEventProvided() throws Exception {
        //        Arrange
        int userId = 1;
        MessageResponse response = new MessageResponse("Event not found!");

        String url = "/eventor/subscriptions/unsubscribe/" + userId;

        //        Act
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(eventRepository.findById(eventInBody.getId())).thenReturn(Optional.empty());

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventInBody)))
                .andExpect(status().isNotFound())
                .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
        Mockito.verify(userRepository, times(1)).findById(userId);
        Mockito.verify(eventRepository, times(1)).findById(eventInBody.getId());
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUnsubscribeToEvent_FailsToUnsubscribe_ValidUserProvided_ValidEventProvided_NoMatchingSubscription() throws Exception {
        //        Arrange
        int userId = 1;

        Set<Subscription> subscriptions = new HashSet<>();
        Subscription subscription = new Subscription(event2, user, df.parse(subscriptionDate));

        subscriptions.add(subscription);

        user.setSubscriptions(subscriptions);

        eventInBody.setLastSubscription(df.parse(subscriptionDate));
        Subscription newSubscription = new Subscription(foundEvent, user, eventInBody.getLastSubscription());
        MessageResponse response = new MessageResponse("You are not following this event!");

        String url = "/eventor/subscriptions/unsubscribe/" + userId;

        //        Act
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(eventRepository.findById(eventInBody.getId())).thenReturn(Optional.of(foundEvent));

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventInBody)))
                        .andExpect(status().isNotFound())
                        .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
        Mockito.verify(userRepository, times(1)).findById(userId);
        Mockito.verify(eventRepository, times(1)).findById(eventInBody.getId());
    }
}
