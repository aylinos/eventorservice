package com.eventor.restController;

import com.eventor.controller.EventController;
import com.eventor.model.Event;
import com.eventor.model.User;
import com.eventor.payload.response.ResponseWithError;
import com.eventor.repository.EventRepository;
import com.eventor.repository.UserRepository;
import com.eventor.security.jwt.JwtUtils;
import com.eventor.security.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(EventController.class)
class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    User user1 = new User("grayb@mail.com", "grayb", "a12347");
    User user2 = new User("emon@gmail.com", "emon", "a12347");

    Event event1 = new Event(user1, "Backyard Party", "Bring drinks!", false, true);
    Event event2 = new Event(user2, "Chess tournament", "Participants aged between 10-80 years old.", true, false);
    Event event3 = new Event(user1, "Java workshop", "Basics", true, false);

    @WithMockUser(roles = "USER")
    @Test
    void testListAllEvents_GetAllEvents_NoTitleProvided() throws Exception {
    //        Arrange
        List<Event> listEvents = new ArrayList<>();
        listEvents.add(event1);
        listEvents.add(event2);
        listEvents.add(event3);

        String url = "/eventor/events";

    //        Act
        Mockito.when(eventRepository.findAll()).thenReturn(listEvents);

    //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(get(url)).andExpect(status().isOk()).andReturn();

//        int status = mvcResult.getResponse().getStatus();
//        System.out.println(status);

        //        Assert
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(listEvents);

    //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testListAllEvents_Get0EventsFromEmptyDb_NoTitleProvided() throws Exception {
    //        Arrange
        List<Event> listEvents = new ArrayList<>();

        String url = "/eventor/events";

    //        Act
        Mockito.when(eventRepository.findAll()).thenReturn(listEvents);

        MvcResult mvcResult = mockMvc.perform(get(url)).andReturn();

    //        Arrange
        int status = mvcResult.getResponse().getStatus();
        System.out.println(status);

    //        Assert status response
        assertEquals(204, status);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testListMyEvents_GetMyEvents_ValidCreatorProvided() throws Exception {
        //        Arrange
        List<Event> listEvents = new ArrayList<>();
        listEvents.add(event1);
        listEvents.add(event3);

        user1.setId(1);
        String url = "/eventor/events/my?";

        ResponseWithError response = new ResponseWithError(listEvents, null);

        //        Act
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(eventRepository.findByCreator(user1)).thenReturn(listEvents);

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                get(url)
                        .param("creator", Integer.toString(user1.getId())))
                        .andExpect(status().isOk())
                        .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testListMyEvents_GetMyEvents_InvalidCreatorProvided() throws Exception {
        //        Arrange
        user1.setId(1);
        String url = "/eventor/events/my?";

        ResponseWithError response = new ResponseWithError(null, "User not found");

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                get(url)
                        .param("creator", Integer.toString(user1.getId())))
                .andExpect(status().isNotFound())
                .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testListMyEvents_GetMyEvents_EmptyList() throws Exception {
        //        Arrange
        List<Event> listEvents = new ArrayList<>();

        user1.setId(1);
        String url = "/eventor/events/my?";

        ResponseWithError response = new ResponseWithError(null, "No events found");

        //        Act
        Mockito.when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        Mockito.when(eventRepository.findByCreator(user1)).thenReturn(listEvents);

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                get(url)
                        .param("creator", Integer.toString(user1.getId())))
                .andExpect(status().isNoContent())
                .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(response);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
    }

        @WithMockUser(roles = {"USER"})
        @Test
        void testGetEventById_ExpectedStatus200() throws Exception{ //With content
            //        Arrange
            long id = 1L;

            String url = "/eventor/events/" + id;

            //        Act
            Mockito.when(eventRepository.findById(id)).thenReturn(java.util.Optional.of(event1));

            //        Act & Assert status response
            MvcResult mvcResult = mockMvc.perform(
                    get(url)).andExpect(status().isOk()).andReturn();

            //Arrange
            String actualJsonResponse = mvcResult.getResponse().getContentAsString();
            System.out.println(actualJsonResponse);

            String expectedJsonResponse = objectMapper.writeValueAsString(event1);

            //        Assert content response
            assertEquals(expectedJsonResponse, actualJsonResponse);

            //        Assert times of database entry
            Mockito.verify(eventRepository, times(1)).findById(id);
        }

        @WithMockUser(roles = {"USER"})
        @Test
        void testGetEventById_ExpectedStatus404() throws Exception{ //Not found
            //        Arrange
            long id = 1L;

            String url = "/eventor/events/" + id;

            //        Act & Assert status response
            MvcResult mvcResult = mockMvc.perform(
                    get(url)).andExpect(status().isNotFound()).andReturn();

            //Arrange
            String actualJsonResponse = mvcResult.getResponse().getContentAsString();
            System.out.println(actualJsonResponse);

            String expectedJsonResponse = "";

            //        Assert content response
            assertEquals(expectedJsonResponse, actualJsonResponse);

            //        Assert times of database entry
            Mockito.verify(eventRepository, times(1)).findById(id);
        }

    @WithMockUser(roles = "USER")
    @Test
    void testCreateEvent_CreateEvent_ValidBodyProvided() throws Exception{
    //        Arrange
        Event newEvent = new Event("One", "One", false);
        Event savedEvent = new Event(1, "One", "One", false);

        String url = "/eventor/events";

    //        Act
        Mockito.when(eventRepository.save(newEvent)).thenReturn(savedEvent);

    //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                post(url)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(newEvent))).andExpect(status().isCreated()).andReturn();
    }

    @WithMockUser(roles = "USER")
    @Test
    void testCreateEvent_FailToCreateEvent_EmptyTitleProvided() throws Exception{ //Bad request - empty Event
    //        Arrange
        Event event = new Event("", "", false);

        String url = "/eventor/events";

    //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                post(url)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(event))).andExpect(status().isBadRequest()).andReturn();
    }

    @WithMockUser(roles = "USER")
    @Test
    void testCreateEvent_FailToCreateEvent_NoBodyProvided() throws Exception{
    //        Arrange
        String url = "/eventor/events";

    //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                post(url)).andExpect(status().isBadRequest()).andReturn();
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateEvent_UpdateEvent_ValidBodyProvided_ValidIdProvided() throws Exception{
    //        Arrange
        long id = 1L;
        Event existEvent = new Event(1, "One", "One", false);
        Event savedEvent = new Event(1, "Oneee", "One", false);

        String url = "/eventor/events/" + id;

    //        Act
        Mockito.when(eventRepository.findById(id)).thenReturn(java.util.Optional.of(existEvent));
        Mockito.when(eventRepository.save(existEvent)).thenReturn(savedEvent);

    //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(existEvent))).andExpect(status().isOk()).andReturn();

    //        Assert times of database entry
        Mockito.verify(eventRepository, times(1)).findById(existEvent.getId());
        Mockito.verify(eventRepository, times(1)).save(existEvent);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateEvent_UpdateEvent_ValidBodyProvided_InvalidIdProvided() throws Exception{
        //        Arrange
        long id = 1L;
        Event existEvent = new Event(1, "One", "One", false);

        String url = "/eventor/events/" + id;

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(existEvent)))
                        .andExpect(status().isNotFound())
                        .andReturn();

        //        Assert times of database entry
        Mockito.verify(eventRepository, times(1)).findById(existEvent.getId());
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateEvent_FailUpdateEvent_ValidBodyProvided_NoIdProvided() throws Exception{
    //        Arrange
        Long id = null;
        Event savedEvent = new Event(1, "Oneee", "One", false);

        String url = "/eventor/events/";

    //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(savedEvent)))
                        .andExpect(status().isMethodNotAllowed())
                        .andReturn();

    //        Assert times of database entry
        Mockito.verify(eventRepository, times(0)).findById(id);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateEvent_UpdateEvent_EmptyTitleProvided_ValidIdProvided() throws Exception{
    //        Arrange
        long id = 1L;
        Event eventToUpdate = new Event(1, "", "One", false);

        String url = "/eventor/events/" + id;

    //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(eventToUpdate))).andExpect(status().isBadRequest()).andReturn();

    //        Assert times of database entry
        Mockito.verify(eventRepository, times(0)).findById(id);
        Mockito.verify(eventRepository, times(0)).save(eventToUpdate);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateEvent_FailToUpdateEvent_NoBodyProvided_ValidIdProvided() throws Exception{
    //        Arrange
        Long id = 1L;

        String url = "/eventor/events";

    //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)).andExpect(status().isMethodNotAllowed()).andReturn();
    }

    @WithMockUser(roles = "USER")
    @Test
    void testDeleteEvent_DeleteFromDb_ValidIdProvided() throws Exception{
    //        Arrange
        Long eventId = 1L;

        String url = "/eventor/events/" + eventId;

    //        Act & Assert status response
        Mockito.doNothing().when(eventRepository).deleteById(eventId);
        mockMvc.perform(delete(url)).andExpect(status().isNoContent());

    //        Assert times of database entry
        Mockito.verify(eventRepository, times(1)).deleteById(eventId);
    }

    @WithMockUser(roles = {"USER", "ADMIN", "MODERATOR"})
    @Test
    void testDeleteEvent_FailToDeleteFromDb_NoIdProvided() throws Exception{
    //        Arrange
        Long eventId = null;
        String url = "/eventor/events/";

//        Compiler never gets here due to lack of path variable
//        Mockito.doNothing().when(eventRepository);

    //        Act & Assert status response
        mockMvc.perform(delete(url)).andExpect(status().isNoContent());

    //        Assert times of database entries
        Mockito.verify(eventRepository, times(0)).deleteById(eventId);
    }

    @WithMockUser(roles = {"ADMIN", "MODERATOR"})
    @Test
    void testDeleteAllEvents_DeleteFromDb() throws Exception{
    //        Arrange
        String url = "/eventor/events/";

    //        Act & Assert status response
        mockMvc.perform(delete(url)).andExpect(status().isNoContent());

    //        Assert times of database entry
        Mockito.verify(eventRepository, times(1)).deleteAll();
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void testListPublishedEvents_ExpectedStatus200() throws Exception{ //With content
        //        Arrange
        List<Event> publishedEvents = new ArrayList<>();
        publishedEvents.add(event2);
        publishedEvents.add(event3);

        String url = "/eventor/events/published";

        //        Act
        Mockito.when(eventRepository.findByPublished(true)).thenReturn(publishedEvents);

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                get(url)).andExpect(status().isOk()).andReturn();

        //Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(publishedEvents);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);

        //        Assert times of database entry
        Mockito.verify(eventRepository, times(1)).findByPublished(true);
    }

    @WithMockUser(roles = {"USER"})
    @Test
    void testListPublishedEvents_ExpectedStatus204() throws Exception{ //Without content
        //        Arrange
        List<Event> publishedEvents = new ArrayList<>();

        String url = "/eventor/events/published";

        //        Act
        Mockito.when(eventRepository.findByPublished(true)).thenReturn(publishedEvents);

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                get(url)).andExpect(status().isNoContent()).andReturn();

        //Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = "";

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);

        //        Assert times of database entry
        Mockito.verify(eventRepository, times(1)).findByPublished(true);
    }
}
