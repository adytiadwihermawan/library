package com.miniproject.library.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "penalty")
@Data
public class Penalty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Integer amount;
    @ManyToOne
    private Loan loan;
}
