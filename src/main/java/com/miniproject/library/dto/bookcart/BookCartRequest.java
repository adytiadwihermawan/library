package com.miniproject.library.dto.bookcart;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class BookCartRequest {
    @NotBlank(message = "Masukkan List Buku")
    private List<Integer> bookIds;
    @NotBlank(message = "Masukkan ID Anggota")
    private Integer anggotaId;
}
