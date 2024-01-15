package com.miniproject.library.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "librarian")
@Data
public class Librarian {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Long nip;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String gender;
}
