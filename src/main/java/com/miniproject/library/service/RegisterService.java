package com.miniproject.library.service;

import com.miniproject.library.dto.register.RegisterRequest;
import com.miniproject.library.entity.*;
import com.miniproject.library.exception.ResourcesBadRequestException;
import com.miniproject.library.repository.AnggotaRepository;
import com.miniproject.library.repository.LibrarianRepository;
import com.miniproject.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterService {
    private final UserRepository userRepository;
    private final LibrarianRepository librarianRepository;
    private final AnggotaRepository anggotaRepository;
    private static final String NIK_OR_NIP_ALREADY_EXIST = "NIK/NIP Already Exist";

    private final ModelMapper mapper = new ModelMapper();

    public User register(RegisterRequest registerRequest, String role){
        role = role.toUpperCase();
        if (userRepository.findByUsername(registerRequest.getUsername()) != null){
            throw new ResourcesBadRequestException(NIK_OR_NIP_ALREADY_EXIST);
        }
        User regis = mapper.map(registerRequest, User.class);
        regis.setPassword(passwordEncoder().encode(registerRequest.getPassword()));
        regis.setRole(Role.valueOf(role));
        if (role.equals("LIBRARIAN")){
            Librarian librarian = mapper.map(registerRequest,Librarian.class);
            librarian.setNip(Long.valueOf(registerRequest.getUsername()));
            librarianRepository.save(librarian);
            regis.setLibrarian(librarian);
            regis.setAnggota(null);
        }else {
            Anggota anggota = mapper.map(registerRequest,Anggota.class);
            anggota.setNik(Long.valueOf(registerRequest.getUsername()));
            anggotaRepository.save(anggota);
            regis.setLibrarian(null);
            regis.setAnggota(anggota);
        }
        //save username dan pw ke tabel user

        userRepository.save(regis);
        return regis;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
