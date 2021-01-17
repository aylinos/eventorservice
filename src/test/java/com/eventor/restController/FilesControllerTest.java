package com.eventor.restController;

import com.eventor.controller.FilesController;
import com.eventor.model.User;
import com.eventor.repository.UserRepository;
import com.eventor.security.jwt.JwtUtils;
import com.eventor.security.services.FilesStorageService;
import com.eventor.security.services.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

@WebMvcTest(FilesController.class)
public class FilesControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FilesStorageService storageService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @MockBean
    private JwtUtils jwtUtils;

    User user = new User("grayb@mail.com", "grayb", "a12347");

    @WithMockUser(roles = "USER")
    @Test
    void testUploadFile_UploadFile_ValidUserIdProvided_ValidImageProvided() throws Exception {
        //  Arrange
        int userId = 1;

        String url = "/eventor/files/upload/users/" + userId;

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        //  Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                multipart(url).file(file))
                .andExpect(status().isOk())
                .andReturn();
    }

    @WithMockUser(roles = "USER")
    @Test
    void testUploadFile_FailToUploadFile_InvalidUserIdProvided_ValidImageProvided() throws Exception {
        //  Arrange
        int userId = 1;

        String url = "/eventor/files/upload/users/" + userId;

        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "hello.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Hello, World!".getBytes()
        );

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        //  Act & Assert status response
        MvcResult mvcResult = mockMvc.perform(
                multipart(url).file(file))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
