package com.miniproject.library.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "loan")
@Data
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateBorrow;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dateReturn;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date dueBorrow;
    @OneToOne(cascade = CascadeType.ALL)
    private BookCart bookCarts;
}
