package com.miniproject.library.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "bookCart")
@Data
public class BookCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Anggota anggota;

    @ManyToMany
    private List<Book> book;

}
