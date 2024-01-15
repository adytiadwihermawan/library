package com.miniproject.library.dto.anggota;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AnggotaRequest {
    @NotNull(message = "Masukkan NIK")
    private Long nik;
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
