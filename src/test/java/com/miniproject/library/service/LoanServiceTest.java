package com.miniproject.library.service;

import com.miniproject.library.dto.bookcart.BookCartRequest;
import com.miniproject.library.dto.loan.LoanResponse;
import com.miniproject.library.entity.Anggota;
import com.miniproject.library.entity.Book;
import com.miniproject.library.entity.BookCart;
import com.miniproject.library.entity.Loan;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class LoanServiceTest {
    @Mock
    private LoanRepository loanRepository;

    @Mock
    private AnggotaRepository anggotaRepository;

    @Mock
    private BookCartRepository bookCartRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PenaltyService penaltyService;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testBorrowBooksWithAvailableBooks() {
        Book book1 = new Book();
        book1.setId(1);
        book1.setStock(1);

        Book book2 = new Book();
        book2.setId(2);
        book2.setStock(2);

        Anggota anggota = new Anggota();
        anggota.setId(1);

        BookCartRequest bookCartRequest = new BookCartRequest();
        bookCartRequest.setAnggotaId(anggota.getId());
        bookCartRequest.setBookIds(List.of(book1.getId(), book2.getId()));

        List<Book> books = List.of(book1, book2);

        BookCart bookCart = new BookCart();
        bookCart.setId(1);
        bookCart.setAnggota(anggota);
        bookCart.setBook(books);

        when(anggotaRepository.findById(1)).thenReturn(Optional.of(anggota));
        when(bookRepository.findAllById(List.of(1, 2))).thenReturn(books);
        when(bookCartRepository.save(any(BookCart.class))).thenReturn(bookCart);
        when(bookCartRepository.findById(any())).thenReturn(Optional.of(bookCart));

        Loan loan = new Loan();
        loan.setId(1);
        loan.setDateBorrow(new Date());
        loan.setDueBorrow(new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000))); // Tambah 7 hari dari waktu peminjaman
        loan.setBookCarts(bookCart);

        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        LoanResponse loanResponse = loanService.borrowBooks(bookCartRequest);


        assertNotNull(loanResponse);
        assertEquals(1, loanResponse.getBookCartId());
        assertNotNull(loan.getId());
        assertNotNull(loan.getDateBorrow());
        assertNotNull(loan.getDueBorrow());

        verify(anggotaRepository).findById(1);
        verify(bookRepository).findAllById(List.of(1, 2));
        verify(bookCartRepository).save(any(BookCart.class));
        verify(bookRepository, times(2)).save(any(Book.class));
        verify(loanRepository).save(any(Loan.class));
    }

    @Test
    void testBorrowBooksWithUnavailableBooks() {
        BookCartRequest bookCartRequest = new BookCartRequest();
        bookCartRequest.setAnggotaId(1);
        bookCartRequest.setBookIds(Arrays.asList(1, 2, 3));

        List<Book> unavailableBooks = Collections.emptyList();

        when(anggotaRepository.findById(1)).thenReturn(Optional.of(new Anggota()));
        when(bookRepository.findAllById(Arrays.asList(1, 2, 3))).thenReturn(unavailableBooks);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            loanService.borrowBooks(bookCartRequest);
        });

        assertEquals("Book Out of Stock", exception.getMessage());
    }

    @Test
    void testBorrowBooks_AnggotaNotFound() {
        BookCartRequest request = new BookCartRequest();
        request.setAnggotaId(999); // ID yang tidak ada

        when(anggotaRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            loanService.borrowBooks(request);
        });
        assertEquals("Id Anggota Not Found", exception.getMessage());
    }
    @Test
    void testBorrowBooks_BookCartNotFound() {
        // Persiapan data untuk pengujian
        BookCartRequest bookCartRequest = new BookCartRequest();
        bookCartRequest.setAnggotaId(1); // ID anggota yang valid
        List<Integer> bookIds = Arrays.asList(1, 2); // ID buku yang valid
        bookCartRequest.setBookIds(bookIds);

        Anggota anggota = new Anggota();
        when(anggotaRepository.findById(1)).thenReturn(Optional.of(anggota));

        List<Book> availableBooks = Arrays.asList(new Book(), new Book());
        when(bookRepository.findAllById(bookIds)).thenReturn(availableBooks);

        // Mock ketika bookCartRepository.findById mengembalikan Optional kosong
        when(bookCartRepository.findById(any())).thenReturn(Optional.empty());

        // Memanggil metode yang ingin diuji dan menangkap pengecualian
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> loanService.borrowBooks(bookCartRequest));

        // Verifikasi bahwa pengecualian terjadi dengan pesan yang diharapkan
        assertNotNull(exception);
        assertEquals("Book Out of Stock", exception.getMessage());
    }

    @Test
    void testBorrowBooks_BooksNotAvailable_AddedToWishlist() {
        BookCartRequest bookCartRequest = new BookCartRequest();
        bookCartRequest.setAnggotaId(1); // ID anggota yang valid
        List<Integer> bookIds = Arrays.asList(1, 2); // ID buku yang tidak tersedia
        bookCartRequest.setBookIds(bookIds);

        Anggota anggota = new Anggota();
        when(anggotaRepository.findById(1)).thenReturn(Optional.of(anggota));

        List<Book> books = Arrays.asList(new Book(), new Book()); // Buku yang tidak tersedia
        when(bookRepository.findAllById(bookIds)).thenReturn(books);

        // Mock ketika bookRepository.save(any(Book.class)) dipanggil
        when(bookRepository.save(any(Book.class))).thenReturn(new Book());

        // Memanggil metode yang ingin diuji dan menangkap pengecualian
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> loanService.borrowBooks(bookCartRequest));

        // Verifikasi bahwa pengecualian terjadi dengan pesan yang diharapkan
        assertNotNull(exception);
        assertEquals("Book Out of Stock", exception.getMessage());

        // Verifikasi bahwa bookRepository.save dipanggil sebanyak buku yang tidak tersedia
        verify(bookRepository, times(books.size())).save(any(Book.class));

        // Verifikasi bahwa nilai wishlist pada setiap buku telah ditingkatkan
        for (Book book : books) {
            assertNotNull(book.getWishlist());
            assertEquals(1, book.getWishlist()); // Memastikan wishlist bertambah sebesar 1
        }
    }

    @Test
    void testGetLoanIdByAnggotaId_WhenLoanExists() {
        // Given
        Integer anggotaId = 1;
        Loan loan = new Loan();
        loan.setId(10);

        when(loanRepository.findLoanAnggota(anggotaId)).thenReturn(Optional.of(loan));

        // When
        Integer result = loanService.getLoanIdByAnggotaId(anggotaId);

        // Then
        assertEquals(loan.getId(), result);
        verify(loanRepository).findLoanAnggota(anggotaId);
    }

    @Test
    void testGetLoanIdByAnggotaId_WhenLoanDoesNotExist() {
        // Given
        Integer anggotaId = 1;

        when(loanRepository.findLoanAnggota(anggotaId)).thenReturn(Optional.empty());

        // When
        Integer result = loanService.getLoanIdByAnggotaId(anggotaId);

        // Then
        assertNull(result);
        verify(loanRepository).findLoanAnggota(anggotaId);
    }

    @Test
    void testReturnBooks() {
        int loanId = 1;

        // Membuat loan mock
        Loan loan = new Loan();
        loan.setId(loanId);
        loan.setDateBorrow(new Date());
        loan.setDueBorrow(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7)));

        // Persiapan bookCart dan books
        BookCart bookCart = new BookCart();
        bookCart.setId(1);
        List<Book> books = new ArrayList<>();
        // Persiapan data buku yang dipinjam dalam loan
        Book book1 = new Book();
        book1.setId(1); // Contoh ID buku yang dipinjam
        books.add(book1); // Menambahkan buku ke daftar peminjaman
        bookCart.setBook(books);
        loan.setBookCarts(bookCart);

        // Mock repository behavior
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        // Persiapan bookIdsReturned yang akan dikembalikan (sebagai contoh, gunakan ID buku yang sama dengan yang dipinjam)
        List<Integer> bookIdsReturned = Arrays.asList(1);

        // Memanggil metode returnBooks
        LoanResponse loanResponse = loanService.returnBooks(loanId, bookIdsReturned, false);

        // Verifikasi behavior
        verify(penaltyService, never()).createPenalty(any(Loan.class), anyInt());
        verify(loanRepository, times(1)).delete(any(Loan.class));
        assertNotNull(loanResponse);
        assertEquals(loanId, loanResponse.getId());
        assertEquals(loan.getDateBorrow(), loanResponse.getDateBorrow());
        assertEquals(loan.getDueBorrow(), loanResponse.getDueBorrow());
        assertEquals(bookCart.getId(), loanResponse.getBookCartId());
    }

    @Test
    void testReturnBooks_WhenLoanNotFound() {
        int loanId = 2;
        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        // Memastikan bahwa saat loanService.returnBooks dipanggil dengan loanId yang tidak ditemukan, akan dilemparkan ResponseStatusException
        assertThrows(ResponseStatusException.class, () -> loanService.returnBooks(loanId, new ArrayList<>(), false));

        // Verifikasi bahwa createPenalty dan save tidak dipanggil karena loan tidak ditemukan
        verify(penaltyService, never()).createPenalty(any(Loan.class), anyInt());
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void testReturnBooks_WhenOverdueAndNotDamaged() {
        int loanId = 1;
        Loan loan = new Loan();
        loan.setId(loanId);

        Date currentDate = new Date();
        Date dueDate = new Date(currentDate.getTime() - TimeUnit.DAYS.toMillis(1)); // Tanggal jatuh tempo sudah lewat
        loan.setDateBorrow(new Date(currentDate.getTime() - TimeUnit.DAYS.toMillis(5))); // Dipinjam 5 hari yang lalu
        loan.setDueBorrow(dueDate);

        // Menambahkan buku dengan ID 1 ke dalam daftar peminjaman
        BookCart bookCart = new BookCart();
        List<Book> books = new ArrayList<>();
        Book book = new Book();
        book.setId(1); // ID buku yang ingin dikembalikan
        books.add(book);
        bookCart.setBook(books);
        loan.setBookCarts(bookCart);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        List<Integer> bookIdsReturned = Arrays.asList(1);


        LoanResponse loanResponse = loanService.returnBooks(loanId, bookIdsReturned, false);

        verify(penaltyService, times(1)).createPenalty(eq(loan), anyInt());
        verify(loanRepository, times(1)).delete(any(Loan.class));
        assertNotNull(loanResponse);
        assertEquals(loanId, loanResponse.getId());
        assertEquals(loan.getDateBorrow(), loanResponse.getDateBorrow());
        assertEquals(loan.getDueBorrow(), loanResponse.getDueBorrow());
        assertEquals(bookCart.getId(), loanResponse.getBookCartId());
    }

    @Test
    void testReturnBooksWithLateAndDamagedBooks() {
        int loanId = 2;
        Loan loan = new Loan();
        loan.setId(loanId);

        Date currentDate = new Date();
        Date dueDate = new Date(currentDate.getTime() - TimeUnit.DAYS.toMillis(1));
        loan.setDateBorrow(new Date(currentDate.getTime() - TimeUnit.DAYS.toMillis(5)));
        loan.setDueBorrow(dueDate);

        // Menambahkan buku dengan ID 1 ke dalam daftar peminjaman
        BookCart bookCart = new BookCart();
        List<Book> books = new ArrayList<>();
        Book book = new Book();
        book.setId(1); // ID buku yang ingin dikembalikan
        books.add(book);
        bookCart.setBook(books);
        loan.setBookCarts(bookCart);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        List<Integer> bookIdsReturned = Arrays.asList(1);

        LoanResponse loanResponse = loanService.returnBooks(loanId, bookIdsReturned, true);

        verify(penaltyService, times(2)).createPenalty(eq(loan), anyInt());
        verify(loanRepository, times(1)).delete(any(Loan.class));
        assertNotNull(loanResponse);
        assertEquals(loanId, loanResponse.getId());
        assertEquals(loan.getDateBorrow(), loanResponse.getDateBorrow());
        assertEquals(loan.getDueBorrow(), loanResponse.getDueBorrow());
        assertEquals(bookCart.getId(), loanResponse.getBookCartId());
    }



    @Test
    void testReturnBooks_DamagedOrLost() {
        Loan loan = new Loan();
        loan.setId(1);
        loan.setDateBorrow(new Date());
        loan.setDueBorrow(new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(5))); // overdue loan
        BookCart bookCart = new BookCart();
        List<Book> books = new ArrayList<>();
        Book damagedBook = new Book();
        damagedBook.setId(1);
        books.add(damagedBook);
        bookCart.setBook(books);
        loan.setBookCarts(bookCart);
        List<Integer> bookIdsReturned = Arrays.asList(1);


        when(loanRepository.findById(1)).thenReturn(Optional.of(loan));

        // Eksekusi
        LoanResponse response = loanService.returnBooks(1,bookIdsReturned, true);

        // Verifikasi
        verify(penaltyService, times(1)).createPenalty(loan, 5000000);
        assertEquals(1, response.getId());
    }
}
