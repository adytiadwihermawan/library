package com.miniproject.library.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "book")
@Data
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String author;
    private String title;
    private String publisher;
    private Date publicationDate;
    private int stock;
    private String summary;
    private Integer wishlist;
    private Integer read;
    private boolean active;  // Menandakan apakah buku aktif atau tidak (ketersediaan)
    @ManyToOne
    private Category category;
}
