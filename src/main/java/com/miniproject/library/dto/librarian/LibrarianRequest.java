package com.miniproject.library.dto.librarian;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LibrarianRequest {
    @NotNull(message = "Masukkan NIK")
    private Long nip;
    @NotBlank(message = "Masukkan Nama")
    private String name;
    @NotBlank(message = "Masukkan email")
    private String email;
    @NotBlank(message = "Masukkan Nomor Telepon")
    private String phone;
    @NotBlank(message = "Masukkan Alamat")
    private String address;
    @NotBlank(message = "Masukkan Jenis Kelamin")
    private String gender;
}
