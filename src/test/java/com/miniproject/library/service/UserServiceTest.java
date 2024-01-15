package com.miniproject.library.service;

import com.miniproject.library.dto.user.UserRequest;
import com.miniproject.library.dto.user.UserResponse;
import com.miniproject.library.entity.User;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class UserServiceTest {
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;
    private final ModelMapper mapper = new ModelMapper();

    private User getUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("123");
        user.setPassword("admin");
        return user;
    }
    private List<User> userList(){
        User user2 = new User();
        user2.setId(2);
        user2.setUsername("1234");
        user2.setPassword("admin");
        return Arrays.asList(getUser(), user2);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getAll() {
        List<User> userList = userList();
        when(userRepository.findAll()).thenReturn(userList);
        List<UserResponse> userResponses = userService.getAll();

        Assertions.assertEquals(userList.size(), userResponses.size());
    }

    @Test
    void testGetByIdUserExists() {
        Integer userId = 1;
        User existingUser = getUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));

        UserResponse userResponse = userService.getById(userId);
        UserResponse expectedResponse = mapper.map(existingUser,UserResponse.class);
        assertNotNull(userResponse);
        Assertions.assertEquals(existingUser.getId(), userResponse.getId());
        Assertions.assertEquals(expectedResponse, userResponse);
    }

    @Test
    void testGetByIdUserNotExists() {
        Integer userId = 66;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.getById(userId));
        Assertions.assertEquals("User Not Found", exception.getMessage());
    }

    @Test
    void testDeleteByIdUserExists() {
        Integer userId = 1;
        User existingUser = getUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        userService.deleteById(userId);
        verify(userRepository, times(1)).deleteById(userId);

    }

    @Test
    void testDeleteByIdUserNotExists() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.deleteById(userId));
        Assertions.assertEquals("User Not Found", exception.getMessage());
        verify(userRepository, never()).deleteById(userId);
    }

    @Test
    void testDeleteByIdError() {
        Integer userId = 1;
        when(userRepository.findById(userId)).thenReturn(Optional.of(getUser()));
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(userRepository).deleteById(userId);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.deleteById(userId));
        Assertions.assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testUpdateByIdUserExists() {
        User user = getUser();
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("123456");
        userRequest.setPassword("new_password");

        User savedUser = new User();
        savedUser.setUsername("1234");
        savedUser.setPassword("new_password");

        // Mock the behavior of your UserRepository
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(getUser())).thenReturn(savedUser);

        // Act
        UserResponse result = userService.updateById(1, userRequest);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("123456", result.getUsername());

        // Verify that the UserRepository's findById and save methods were called with the correct arguments
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateByIdUserNotFound() {
        Integer userId = 1;
        UserRequest userRequest = new UserRequest();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> userService.updateById(userId, userRequest));

        assertEquals("User Not Found", exception.getMessage());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any());
    }
}