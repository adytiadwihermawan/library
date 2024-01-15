package com.miniproject.library.controller;

import com.miniproject.library.dto.bookcart.BookCartRequest;
import com.miniproject.library.dto.loan.LoanResponse;
import com.miniproject.library.service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.web.server.ResponseStatusException;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class LoanControllerTest {

    private MockMvc mockMvc;

    @Mock
    private LoanService loanService;

    @InjectMocks
    private LoanController loanController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testBorrowBooksEndpoint() throws Exception {
        BookCartRequest request = new BookCartRequest();

        LoanResponse loanResponse = LoanResponse.builder()
                .id(1)
                .dateBorrow(new Date())
                .dueBorrow(new Date())
                .dateReturn(new Date())
                .bookCartId(1)
                .build();

        when(loanService.borrowBooks(request)).thenReturn(loanResponse);

        ResponseEntity<LoanResponse> responseEntity = loanController.borrowBooks(request);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(loanResponse, responseEntity.getBody());
    }


    @Test
    public void testReturnBooks_Success() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, InvocationTargetException {
        // Mock data
        int loanId = 123;
        List<Integer> bookIdsReturned = Arrays.asList(1, 2, 3);
        boolean isDamagedOrLost = false;

        // Mock LoanService
        LoanService loanService = mock(LoanService.class);

        // Create a LoanResponse object using reflection
        LoanResponse mockResponse = LoanResponse.builder()
                .bookCartId(1)
                .overdueFine(0)
                .dateBorrow(new Date())
                .dueBorrow(new Date(+1))
                .id(1)
                .build();

        // Mock the service method call
        when(loanService.returnBooks(eq(loanId), eq(bookIdsReturned), eq(isDamagedOrLost))).thenReturn(mockResponse);

        // Instantiate LoanController with the mocked LoanService
        LoanController loanController = new LoanController(loanService);

        // Call the controller method
        ResponseEntity<LoanResponse> responseEntity = loanController.returnBooks(loanId, bookIdsReturned, isDamagedOrLost);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
    }

    @Test
    public void testGetLoanIdByAnggotaId_WhenLoanExists() {
        // Given
        Integer anggotaId = 1;
        Integer loanId = 10;

        when(loanService.getLoanIdByAnggotaId(anggotaId)).thenReturn(loanId);

        // When
        ResponseEntity<Integer> responseEntity = loanController.getLoanIdByAnggotaId(anggotaId);

        // Then
        assertSame(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(loanId, responseEntity.getBody());
        verify(loanService).getLoanIdByAnggotaId(anggotaId);
    }

    @Test
    public void testGetLoanIdByAnggotaId_Success() {
        // Mock data
        int anggotaId = 123;
        int expectedLoanId = 456;

        // Mock LoanService
        LoanService loanService = mock(LoanService.class);
        when(loanService.getLoanIdByAnggotaId(anggotaId)).thenReturn(expectedLoanId);

        // Instantiate LoanController with the mocked LoanService
        LoanController loanController = new LoanController(loanService);

        // Call the controller method
        ResponseEntity<Integer> responseEntity = loanController.getLoanIdByAnggotaId(anggotaId);

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedLoanId, responseEntity.getBody());
    }
}
