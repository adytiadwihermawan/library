package com.miniproject.library.dto.book;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Date;

@Data
public class BookRequest {
    @NotBlank(message = "Masukkan Pengarang")
    private String author;
    @NotBlank(message = "Masukkan Penerbit")
    private String publisher;
    @NotNull(message = "Masukkan Tahun Terbit")
    private Date publicationDate;
    @NotNull(message = "Masukkan Stok")
    private Integer stock;
    @NotBlank(message = "Masukkan Judul")
    private String title;
    @NotBlank(message = "Masukkan Summary")
    private String summary;
    @NotNull(message = "Masukkan Kategori")
    private Integer categoryId;
}
