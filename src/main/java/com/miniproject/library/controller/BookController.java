package com.miniproject.library.controller;

import com.miniproject.library.dto.book.BookRequest;
import com.miniproject.library.dto.book.BookResponse;
import com.miniproject.library.entity.Book;
import com.miniproject.library.util.BookReport;
import com.miniproject.library.service.BookService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Tag(name = "Book")
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;

    @PostMapping
    public ResponseEntity<BookResponse> addBook(@RequestBody BookRequest request){
        BookResponse bookResponse = bookService.addBook(request);
        return new ResponseEntity<>(bookResponse, HttpStatus.CREATED);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable Integer id, @Valid
    @RequestBody BookRequest request){
        BookResponse bookResponse = bookService.updateBook(request, id);
        return ResponseEntity.ok(bookResponse);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Book>> getAllBook(){
        List<Book> books = bookService.getAllBook();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id){
        Book book = bookService.getBookByIdBook(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<byte[]> generateBookReport() {
            List<Book> bookList = bookService.getAllBook();
            BookReport pdfService = new BookReport();
            byte[] pdfContent = pdfService.generateBookReport(bookList);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "BookReport.pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
    }
}
