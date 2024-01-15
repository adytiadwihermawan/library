package com.miniproject.library.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CategoryRequest {
    @NotBlank(message = "Masukkan Nama Kategori")
    private String name;
}
