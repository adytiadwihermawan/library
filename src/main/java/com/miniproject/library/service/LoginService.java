package com.miniproject.library.service;

import com.miniproject.library.security.UserDetailServiceImpl;
import com.miniproject.library.dto.login.LoginRequest;
import com.miniproject.library.dto.login.LoginResponse;
import com.miniproject.library.entity.User;
import com.miniproject.library.repository.UserRepository;
import com.miniproject.library.util.JwtToken;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginService {
    private final UserRepository userRepository;
    private final UserDetailServiceImpl userDetailService;

    public LoginResponse login(@Valid LoginRequest userRequest){
        UserDetails userDetails = userDetailService.loadUserByUsername(userRequest.getUsername());

        String password = userRequest.getPassword();
        String password2 = userDetails.getPassword();
        if (!passwordEncoder().matches(password, password2)) {
            throw new BadCredentialsException("Invalid password");
        }
        UsernamePasswordAuthenticationToken.authenticated(userDetails,userDetails.isCredentialsNonExpired(),userDetails.getAuthorities());
        User user = userRepository.findByUsername(userDetails.getUsername());
        String token = JwtToken.getToken(user);
        String username = user.getUsername();
        LoginResponse response = new LoginResponse();
        response.setUsername(username);
        response.setRoles(userDetails.getAuthorities().toString());
        response.setToken(token);
        return response;
    }
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
