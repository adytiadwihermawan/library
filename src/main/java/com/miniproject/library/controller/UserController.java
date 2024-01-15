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
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Users")
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    private final RegisterService registerService;

    private final LoginService loginService;

    @PutMapping("/edit-{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Integer id, @Valid @RequestBody UserRequest userRequest) {
            UserResponse updatedUser = userService.updateById(id, userRequest);
            return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Integer id) {
            UserResponse user = userService.getById(id);
            return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete-{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Integer id){
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted successfully");
    }

    @PostMapping("/register/{role}")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request,@PathVariable String role){
        User response = registerService.register(request,role);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, @RequestHeader HttpHeaders headers ){
        LoginResponse response = loginService.login(request);
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + response.getToken());
        return ResponseEntity.ok(response);
    }
}
