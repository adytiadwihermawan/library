package com.miniproject.library.controller;

import com.miniproject.library.dto.login.LoginRequest;
import com.miniproject.library.dto.login.LoginResponse;
import com.miniproject.library.dto.register.RegisterRequest;
import com.miniproject.library.dto.user.UserRequest;
import com.miniproject.library.dto.user.UserResponse;
import com.miniproject.library.entity.User;
import com.miniproject.library.service.LoginService;
import com.miniproject.library.service.RegisterService;
import com.miniproject.library.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@RunWith(MockitoJUnitRunner.class)
class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserService userService;

    @Mock
    LoginService loginService;
    @Mock
    RegisterService registerService;

    private final ModelMapper mapper = new ModelMapper();
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private User getUser(){
        User user = new User();
        user.setId(1);
        user.setUsername("2345");
        user.setPassword("admin");
        return user;
    }
    private List<User> userList(){
        List<User> userList = new ArrayList<>();
        User user2 = new User();
        user2.setId(2);
        user2.setUsername("23451");
        user2.setPassword("admin");
        userList.add(user2);
        userList.add(getUser());
        return userList;
    }
    @Test
    void updateUser() {
        User user = getUser();
        UserRequest userRequest = new UserRequest();
        userRequest.setUsername("1234");
        userRequest.setPassword("123");
        user.setUsername(userRequest.getUsername());
        userRequest.setPassword(userRequest.getPassword());
        UserResponse expectedUpdatedUser = mapper.map(user, UserResponse.class);
        when(userService.updateById(1, userRequest)).thenReturn(expectedUpdatedUser);
        ResponseEntity<UserResponse> responseEntity = userController.updateUser(1,userRequest);

        // Asserting the response
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedUpdatedUser, responseEntity.getBody());
    }

    @Test
    void getAllUsers() {
        List<UserResponse> response = userList().stream().map(user -> mapper.map(user,UserResponse.class)).collect(Collectors.toList());

        when(userService.getAll()).thenReturn(response);

        ResponseEntity<List<UserResponse>> responseEntity = userController.getAllUsers();
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertIterableEquals(response, responseEntity.getBody());
    }

    @Test
    void getUserById() {
        UserResponse expectedUserResponse = mapper.map(getUser(),UserResponse.class);
        when(userService.getById(1)).thenReturn(expectedUserResponse);
        ResponseEntity<UserResponse> responseEntity = userController.getUserById(1);
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedUserResponse, responseEntity.getBody());
    }

    @Test
    void deleteUser() {
        User user= getUser();
        doNothing().when(userService).deleteById(getUser().getId());
        ResponseEntity<?> responseEntity = userController.deleteUser(user.getId());
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        Assertions.assertEquals("User deleted successfully", responseEntity.getBody());

        verify(userService, times(1)).deleteById(getUser().getId());
    }

    @Test
    void testUpdateUserWithBlankUsername() {
        // Given
        Integer userId = 1;
        UserRequest requestWithBlankUsername = new UserRequest();
        requestWithBlankUsername.setUsername("");
        requestWithBlankUsername.setPassword("admin");

        // Mocking the behavior of the service
        when(userService.updateById(eq(userId), any()))
                .thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username cannot be blank"));

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userController.updateUser(userId, requestWithBlankUsername));

        // Then
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Username cannot be blank", exception.getReason());

        // Verify that the service method was called
        verify(userService, times(1)).updateById(eq(userId), any());
    }

    @Test
    void testLogin() {
        LoginRequest userRequest = new LoginRequest();
        userRequest.setUsername("aku anak dewa coding");
        userRequest.setPassword("admin");
        User expectedUser = getUser();
        LoginResponse expectedResponse = mapper.map(expectedUser, LoginResponse.class);
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + expectedResponse.getToken());

        when(loginService.login(any())).thenReturn(expectedResponse);
        ResponseEntity<LoginResponse> responseEntity = userController.login(userRequest,headers);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());

        // Verify that loginService.login is called once with the provided userRequest
        verify(loginService, times(1)).login((userRequest));
    }

    @Test
    void testRegisterVisitor() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setUsername("12345");
        registerRequest.setPassword("admin");

        User expectedUser = new User();
        expectedUser.setId(1);
        expectedUser.setUsername("123");
        expectedUser.setPassword("admin");

        String role="VISITOR";
        when(registerService.register(registerRequest,role)).thenReturn(expectedUser);
        ResponseEntity<User> responseEntity = userController.register(registerRequest,role);

        Assertions.assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        Assertions.assertEquals(expectedUser, responseEntity.getBody());

        verify(registerService, times(1)).register(registerRequest,role);
    }

}