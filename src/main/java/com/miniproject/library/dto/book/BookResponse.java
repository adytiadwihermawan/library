package com.miniproject.library.dto.book;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public class BookResponse {
    private Integer id;
    private String author;
    private String publisher;
    private Date publicationDate;
    private Integer stock;
    private String title;
    private String summary;
    private String categoryName;
}
