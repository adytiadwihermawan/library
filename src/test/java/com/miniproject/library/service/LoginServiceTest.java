package com.miniproject.library.service;

import com.miniproject.library.security.UserDetailServiceImpl;
import com.miniproject.library.dto.login.LoginRequest;
import com.miniproject.library.dto.login.LoginResponse;
import com.miniproject.library.entity.Anggota;
import com.miniproject.library.entity.Role;
import com.miniproject.library.entity.User;
import com.miniproject.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
 class LoginServiceTest {
   @Mock
   UserDetails userDetails;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserDetailServiceImpl userDetailService;

    @InjectMocks
    LoginService loginService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
     void testLogin() {
        LoginRequest userRequest = new LoginRequest();
        userRequest.setUsername("123456");
        userRequest.setPassword("testPassword");

        User user = new User();
        user.setUsername("123456");
        user.setPassword(loginService.passwordEncoder().encode("testPassword"));
        user.setId(1);
        user.setAnggota(new Anggota());
        user.setRole(Role.valueOf("VISITOR"));
        user.setLibrarian(null);
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_VISITOR"));
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);

        // Mocking UserRepository
        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(user);

        // Mocking UserDetailService
        when(userDetailService.loadUserByUsername(userRequest.getUsername())).thenReturn(userDetails);

        LoginResponse response = loginService.login(userRequest);

        // Assertions
        assertEquals("123456", response.getUsername());
        assertEquals("[ROLE_VISITOR]", response.getRoles());
        assertNotNull(response.getToken());
    }

    @Test
    void invalidPassword()  {
        LoginRequest userRequest = new LoginRequest();
        userRequest.setUsername("12345");
        userRequest.setPassword("testPassword");
        User user = new User();
        user.setUsername(userRequest.getUsername());
        user.setPassword("12345");
        user.setId(1);
        user.setAnggota(new Anggota());
        user.setRole(Role.valueOf("VISITOR"));
        user.setLibrarian(null);

        when(userRepository.findByUsername(userRequest.getUsername())).thenReturn(user);
        when(userDetailService.loadUserByUsername("12345")).thenReturn(userDetails);
        assertThrows(BadCredentialsException.class, () ->
                loginService.login(userRequest)
        );
    }

}
