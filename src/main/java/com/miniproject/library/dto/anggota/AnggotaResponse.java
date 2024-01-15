package com.miniproject.library.dto.anggota;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AnggotaResponse {
    private Integer id;
    private Long nik;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;
}
