package com.eventor.restController;

import com.eventor.controller.UserController;
import com.eventor.model.Event;
import com.eventor.model.User;
import com.eventor.repository.EventRepository;
import com.eventor.repository.RoleRepository;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventRepository eventRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    User user1 = new User("grayb@mail.com", "grayb", "a12347");
    User user2 = new User("emon@gmail.com", "emon", "a12347");
    User user3 = new User("malpaul@yahoo.com", "malpaul", "a12347");

    @WithMockUser(roles = "USER")
    @Test
    void testListAllUsers_GetAllUsers() throws Exception {
        //        Arrange
        List<User> listUsers = new ArrayList<>();
        listUsers.add(user1);
        listUsers.add(user2);
        listUsers.add(user3);

        String url = "/eventor/users/all";

        //        Act
        Mockito.when(userRepository.findAll()).thenReturn(listUsers);

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = objectMapper.writeValueAsString(listUsers);

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testListAllUsers_Get0UsersFromEmptyDb() throws Exception {
        //        Arrange
        List<User> listUsers = new ArrayList<>();

        String url = "/eventor/users/all";

        //        Act
        Mockito.when(userRepository.findAll()).thenReturn(listUsers);

        MvcResult mvcResult = mockMvc.perform(get(url))
                .andExpect(status().isNoContent())
                .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = "";

        //        Assert content response
        assertEquals(expectedJsonResponse, actualJsonResponse);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateUser_FailUpdateUser_ValidBodyProvided_ValidIdProvided_SetNonUniqueProperty() throws Exception{
        //        Arrange
        int id = 1;
        User foundUser = user1;
        User updatedUser = user1;
        updatedUser.setFirstName("Gray");

        String url = "/eventor/users/" + id;

        //        Act
        Mockito.when(userRepository.findById(id)).thenReturn(java.util.Optional.of(foundUser));
        Mockito.when(userRepository.save(foundUser)).thenReturn(updatedUser);

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(foundUser))).andExpect(status().isOk()).andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = "Your information has been updated successfully!";

        //        Assert content response
        assertTrue(actualJsonResponse.contains(expectedJsonResponse));

        //        Assert times of database entry
        Mockito.verify(userRepository, times(1)).findById(id);
        Mockito.verify(userRepository, times(1)).save(foundUser);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateUser_UpdateUser_ValidBodyProvided_ValidIdProvided_SetUniqueProperty() throws Exception{
        //        Arrange
        int id = 1;
        User foundUser = user1;
        User updatedUser = new User("emon@gmail.com", "grayb", "a12347");

        String url = "/eventor/users/" + id;

        //        Act
        Mockito.when(userRepository.findById(id)).thenReturn(java.util.Optional.of(foundUser));
        Mockito.when(userRepository.existsByEmail(updatedUser.getEmail())).thenReturn(true);

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedUser)))
                        .andExpect(status().isExpectationFailed())
                        .andReturn();

        //        Arrange
        String actualJsonResponse = mvcResult.getResponse().getContentAsString();
        System.out.println(actualJsonResponse);

        String expectedJsonResponse = "This email is already registered by other user!";

        //        Assert content response
        assertTrue(actualJsonResponse.contains(expectedJsonResponse));

        //        Assert times of database entry
        Mockito.verify(userRepository, times(1)).findById(id);
        Mockito.verify(userRepository, times(1)).existsByEmail(updatedUser.getEmail());
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateUser_FailUpdateUser_ValidBodyProvided_NoIdProvided() throws Exception{
        String url = "/eventor/users/";

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user1)))
                        .andExpect(status().isNotFound())
                        .andReturn();

        //        Assert times of database entry
        Mockito.verify(userRepository, times(0)).findById(null);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateUser_FailUpdateUser_ValidBodyProvided_InvalidIdProvided() throws Exception{
        //Arrange
        int id = 300;

//        Mockito.doNothing().when(userRepository.findById(id));
        Mockito.when(userRepository.findById(id)).thenReturn(Optional.empty());

        String url = "/eventor/users/" + id;

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(user1)))
                        .andExpect(status().isNotFound())
                        .andReturn();

        //        Assert times of database entry
        Mockito.verify(userRepository, times(1)).findById(id);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateUser_FailUpdateUser_EmptyEmailProvided_ValidIdProvided() throws Exception{
        //        Arrange
        int id = 1;
        User foundUser = user1;
        User updatedUser = new User("", "blan", "a12347");

        Mockito.when(userRepository.findById(id)).thenReturn(java.util.Optional.of(foundUser));

        String url = "/eventor/users/" + id;

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updatedUser)))
                        .andExpect(status().isExpectationFailed())
                        .andReturn();

        //        Assert times of database entry
        Mockito.verify(userRepository, times(1)).findById(id);
        Mockito.verify(userRepository, times(0)).save(updatedUser);
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateUser_FailToUpdateUser_NoBodyProvided_ValidIdProvided() throws Exception{
        //        Arrange
        int id = 1;
        User foundUser = user1;

        Mockito.when(userRepository.findById(id)).thenReturn(java.util.Optional.of(foundUser));

        String url = "/eventor/users/" + id;

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)).andExpect(status().isBadRequest()).andReturn();
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUpdateUser_FailToUpdateUser_NoBodyProvided_NoIdProvided() throws Exception{
        //        Arrange

        String url = "/eventor/users";

        //        Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                put(url)).andExpect(status().isNotFound()).andReturn();

        //        Assert times of database entry
        Mockito.verify(userRepository, times(0)).findById(null);
    }
}
