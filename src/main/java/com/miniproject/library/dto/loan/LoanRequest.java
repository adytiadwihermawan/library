package com.miniproject.library.dto.loan;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class LoanRequest {
    @NotBlank(message = "Masukkan Tanggal dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateBorrow;
    @NotBlank(message = "Masukkan Tanggal dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateReturn;
    @NotBlank(message = "Masukkan Tanggal dd/MM/yyyy")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dueBorrow;
    @NotBlank(message = "Masukkan List ID buku")
    private List<Integer> bookCartId;
    @NotBlank(message = "Masukkan ID pustakawan")
    private Integer librarianId;
    @NotBlank(message = "Masukkan ID Anggota")
    private Integer anggotaId;
}
