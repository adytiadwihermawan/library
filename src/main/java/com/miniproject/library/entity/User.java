package com.miniproject.library.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Table(name = "users")
@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    @JsonIgnore
    private String password;

    @OneToOne
    private Anggota anggota;

    @OneToOne
    private Librarian librarian;

    @Enumerated(EnumType.STRING)
    private Role role;
}
