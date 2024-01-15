package com.miniproject.library.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "anggota")
@Data
public class Anggota {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long nik;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;
}
