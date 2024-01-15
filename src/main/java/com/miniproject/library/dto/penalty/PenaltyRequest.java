package com.miniproject.library.dto.penalty;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PenaltyRequest {
    @NotBlank(message = "Masukkan Nama")
    private String name;
    @NotBlank(message = "Masukkan Deskripsi")
    private String description;
    @NotBlank(message = "Masukkan biaya")
    private Integer cost;
    @NotBlank(message = "Masukkan ID peminjaman")
    private Integer loanId;
    @NotBlank(message = "Masukkan Total")
    private Double amount;
    @NotBlank(message = "Masukkan Status")
    private boolean damaged;
    @NotBlank(message = "Masukkan Status")
    private boolean lost;
}
