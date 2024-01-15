package com.miniproject.library.dto.register;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRequest {
    @NotBlank(message = "Masukkan Username")
    private String username;
    @NotBlank(message = "Masukkan Password")
    private String password;
    @NotBlank(message = "Masukkan nama")
    private String name;
    @NotBlank(message = "Masukkan email")
    private String email;
    @NotBlank(message = "Masukkan nomer telepon")
    private String phone;
    @NotBlank(message = "Masukkan alamat")
    private String address;
    @NotBlank(message = "Masukkan jenis kelamin")
    private String gender;
}
