package com.miniproject.library.controller;

import com.miniproject.library.dto.book.BookRequest;
import com.miniproject.library.dto.book.BookResponse;
import com.miniproject.library.entity.Book;
import com.miniproject.library.entity.Category;
import com.miniproject.library.util.BookReport;
import com.miniproject.library.service.BookService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookControllerTest {
    @InjectMocks
    BookController bookController;
    @Mock
    BookService bookService;
    @Mock
    BookReport bookReport;

    private final ModelMapper mapper = new ModelMapper();
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    Date date = new Date(2001-22-2);

    private List<Book> mockBooks(){
        Category category = new Category();
        category.setName("Fiction");
        category.setId(2);
        Book book = mapper.map(bookRequest(),Book.class);
        Book existingBook = new Book();
        existingBook.setId(1);
        existingBook.setCategory(category);
        existingBook.setPublicationDate(date);
        existingBook.setAuthor("author");
        existingBook.setPublisher("publisher");
        existingBook.setActive(true);
        existingBook.setSummary("summary");
        existingBook.setRead(0);
        existingBook.setStock(69);
        existingBook.setWishlist(0);
        List<Book> books = new ArrayList<>();
        books.add(existingBook);
        books.add(book);
        return books;
    }

    private BookRequest bookRequest(){
        Category category = new Category();
        category.setName("Fiction");
        category.setId(2);
        BookRequest request = new BookRequest();
        request.setAuthor("Makoto Shinkai");
        request.setPublisher("Kyoto Animation");
        request.setStock(20);
        request.setPublicationDate(date);
        request.setTitle("weathering with you");
        request.setSummary("Pilih cinta atau kiamat? ya cintalah");
        request.setCategoryId(2);
        return request;
    }
    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void addBook() {
        BookRequest request = bookRequest();
        Book book = mapper.map(request,Book.class);
        BookResponse response = mapper.map(book,BookResponse.class);
        when(bookService.addBook(bookRequest())).thenReturn(response);
        ResponseEntity<BookResponse> responseEntity = bookController.addBook(bookRequest());

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void updateBook() {
        BookResponse response =mapper.map(mockBooks().get(0),BookResponse.class);
        when(bookService.updateBook(bookRequest(),1)).thenReturn(response);
        ResponseEntity<BookResponse> responseEntity = bookController.updateBook(1, bookRequest());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(response, responseEntity.getBody());
    }

    @Test
    void getAllBook() {

        when(bookService.getAllBook()).thenReturn(mockBooks());

        // Call the controller method
        ResponseEntity<List<Book>> responseEntity = bookController.getAllBook();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockBooks(), responseEntity.getBody());
    }

    @Test
    void getBookById() {
        when(bookService.getBookByIdBook(1)).thenReturn(mockBooks().get(0));
        ResponseEntity<Book> responseEntity = bookController.getBookById(1);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockBooks().get(0), responseEntity.getBody());
    }

}
