package com.miniproject.library.service;

import com.miniproject.library.dto.book.BookRequest;
import com.miniproject.library.dto.book.BookResponse;
import com.miniproject.library.entity.Book;
import com.miniproject.library.entity.Category;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.BookRepository;
import com.miniproject.library.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookService bookService;
    Date date = new Date(123, Calendar.FEBRUARY,1);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBook() {
        BookRequest request = new BookRequest();
        request.setAuthor("Makoto Shinkai");
        request.setPublisher("CoMix Wave Films");
        request.setStock(10);
        request.setTitle("Kimi no nawa");
        request.setSummary("CINTA BEDA DIMENSI");
        request.setCategoryId(1);

        Category category = new Category();
        category.setId(1);
        category.setName("Fiction");

        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

        BookResponse response = bookService.addBook(request);
        response.setId(1);

        assertNotNull(response.getId());
        assertNotEquals(0, response.getId());
        assertEquals("Makoto Shinkai", response.getAuthor());
        assertEquals("CoMix Wave Films", response.getPublisher());
        assertEquals(10, response.getStock());
        assertEquals("Kimi no nawa", response.getTitle());
        assertEquals("CINTA BEDA DIMENSI", response.getSummary());
        assertEquals("Fiction", response.getCategoryName());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testUpdateBook() {
        Category category = new Category();
        category.setName("Fiction");
        category.setId(2);

        Integer bookId = 1;
        BookRequest request = new BookRequest();
        request.setAuthor("Makoto Shinkai");
        request.setPublisher("Kyoto Animation");
        request.setStock(20);
        request.setPublicationDate(date);
        request.setTitle("weathering with you");
        request.setSummary("Pilih cinta atau kiamat? ya cintalah");
        request.setCategoryId(2);

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setCategory(new Category());
        existingBook.setPublicationDate(date);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.of(category));

        BookResponse response = bookService.updateBook(request, bookId);
        response.setCategoryName(category.getName());

        assertNotNull(response);
        assertEquals(bookId, response.getId());
        assertEquals("Makoto Shinkai", response.getAuthor());
        assertEquals("Kyoto Animation", response.getPublisher());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2023-02-01", dateFormat.format(date));
        assertEquals(20, response.getStock());
        assertEquals("weathering with you", response.getTitle());
        assertEquals("Pilih cinta atau kiamat? ya cintalah", response.getSummary());
        assertEquals("Fiction", response.getCategoryName());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void testAddBookCategoryNotFound() {
        BookRequest request = new BookRequest();
        request.setCategoryId(1);

        when(categoryRepository.findById(request.getCategoryId())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bookService.addBook(request));

        assertEquals("Id Category Not Found", exception.getMessage());
    }

    @Test
    void testUpdateBookNotFound() {
        Integer bookId = 1;
        BookRequest request = new BookRequest();
        request.setAuthor("HAJIME ISAYAMA");
        request.setPublisher("Mappa Studio");
        request.setStock(10);
        request.setTitle("Attack On Titan");
        request.setSummary("Ending gk jelas");
        request.setCategoryId(1);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bookService.updateBook(request, bookId));

        assertEquals("Id Book Not Found", exception.getMessage());
    }

    @Test
    void testGetAllBook() {
        Book sampleBook = getBook();
        List<Book>expectedBookList = new ArrayList<>();
        expectedBookList.add(sampleBook);


        when(bookRepository.findAll()).thenReturn(expectedBookList);

        List<Book> bookList = bookService.getAllBook();

        assertFalse(bookList.isEmpty());


        Book book = bookList.get(0);
        assertNotNull(book);
        assertEquals("Makoto Shinkai", book.getAuthor());
        assertEquals("Kyoto Animation", book.getPublisher());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2023-02-01", dateFormat.format(date));
        assertEquals(20, book.getStock());
        assertEquals("weathering with you", book.getTitle());
        assertEquals("Pilih cinta atau kiamat? ya cintalah", book.getSummary());
        assertEquals("Fiction", book.getCategory().getName());
        verify(bookRepository, times(1)).findAll();

    }

    private Book getBook() {
        Category category = new Category();
        category.setId(1);
        category.setName("Fiction");
        Book sampleBook = new Book();
        sampleBook.setId(1);
        sampleBook.setAuthor("Makoto Shinkai");
        sampleBook.setPublisher("Kyoto Animation");
        sampleBook.setStock(20);
        sampleBook.setPublicationDate(date);
        sampleBook.setTitle("weathering with you");
        sampleBook.setSummary("Pilih cinta atau kiamat? ya cintalah");
        sampleBook.setCategory(category);
        return sampleBook;
    }

    @Test
    void testGetBookByIdBook() {
        Category category = new Category();
        category.setId(1);
        category.setName("Fiction");

        Integer bookId = 1;
        Book sampleBook = new Book();
        sampleBook.setId(bookId);
        sampleBook.setAuthor("Makoto Shinkai");
        sampleBook.setPublisher("Kyoto Animation");
        sampleBook.setStock(20);
        sampleBook.setPublicationDate(date);
        sampleBook.setTitle("weathering with you");
        sampleBook.setSummary("Pilih cinta atau kiamat? ya cintalah");
        sampleBook.setCategory(category);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(sampleBook));

        Book book = bookService.getBookByIdBook(bookId);

        assertNotNull(book);
        assertEquals(bookId, book.getId());
        assertEquals("Makoto Shinkai", book.getAuthor());
        assertEquals("Kyoto Animation", book.getPublisher());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        assertEquals("2023-02-01", dateFormat.format(date));
        assertEquals(20, book.getStock());
        assertEquals("weathering with you", book.getTitle());
        assertEquals("Pilih cinta atau kiamat? ya cintalah", book.getSummary());
        assertEquals("Fiction", book.getCategory().getName());

        verify(bookRepository, times(1)).findById(bookId);
    }

    @Test
    void testGetBookByIdBookNotFound() {
        Integer bookId = 1;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bookService.getBookByIdBook(bookId));

        assertEquals("Id Book Not Found", exception.getMessage());
    }

    @Test
     void testUpdateBookWhenBookNotFound() {
        // Arrange
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act and Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> bookService.updateBook(new BookRequest(), 1));
        assertEquals("Id Book Not Found", exception.getMessage());

    }

    @Test
     void testUpdateBookWhenCategoryNotFound() {
        // Arrange
        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(new Book()));
        when(categoryRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ResourceNotFoundException.class, () ->
            bookService.updateBook(new BookRequest(), 1));
    }
}
