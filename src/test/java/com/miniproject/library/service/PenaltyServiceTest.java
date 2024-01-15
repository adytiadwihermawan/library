package com.miniproject.library.service;

import com.miniproject.library.dto.penalty.PenaltyResponse;
import com.miniproject.library.entity.Loan;
import com.miniproject.library.entity.Penalty;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.LoanRepository;
import com.miniproject.library.repository.PenaltyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PenaltyServiceTest {

    @Mock
    private PenaltyRepository penaltyRepository;

    @InjectMocks
    private PenaltyService penaltyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllPenalties() {
        // Arrange
        List<Penalty> penaltyList = new ArrayList<>();
        Penalty penalty1 = new Penalty();
        penalty1.setId(1);
        penalty1.setAmount(50);
        penaltyList.add(penalty1);

        Penalty penalty2 = new Penalty();
        penalty2.setId(2);
        penalty2.setAmount(75);
        penaltyList.add(penalty2);

        when(penaltyRepository.findAll()).thenReturn(penaltyList);

        // Act
        List<Penalty> result = penaltyService.getAllPenalties();

        // Assert
        assertEquals(penaltyList, result);
    }

    @Test
    void testCreatePenalty() {
        Loan loan = new Loan();
        loan.setId(1); // Contoh ID Loan
        loan.setDateBorrow(new Date()); // Tanggal pinjam
        loan.setDueBorrow(new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000)));
        Integer amount = 100;

        Penalty penalty = new Penalty();
        penalty.setId(1);
        penalty.setLoan(loan);
        penalty.setAmount(amount);

        when(penaltyRepository.save(any(Penalty.class))).thenReturn(penalty);

        PenaltyResponse result = penaltyService.createPenalty(loan, amount);

        assertNotNull(result);
        assertEquals(penalty.getAmount(), result.getAmount());
        assertEquals(penalty.getLoan().getId(), result.getLoanId());
    }

    @Test
    void testGetPenaltyById() {
        // Arrange
        int penaltyId = 1;
        Penalty penalty = new Penalty();
        penalty.setId(penaltyId);
        penalty.setAmount(50);

        Loan loan = new Loan();
        loan.setId(1);
        loan.setDateBorrow(new Date());
        loan.setDueBorrow(new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000)));

        penalty.setLoan(loan);

        when(penaltyRepository.findById(penaltyId)).thenReturn(Optional.of(penalty));

        // Act
        PenaltyResponse result = penaltyService.getPenaltyById(penaltyId);

        // Assert
        assertNotNull(result);
        assertEquals(penalty.getId(), result.getId());
        assertEquals(penalty.getAmount(), result.getAmount());
        assertEquals(penalty.getLoan().getId(), result.getLoanId());
    }

    @Test
    void testGetPenaltyById_NotFound() {
        // Arrange
        int penaltyId = 999; // Id yang tidak ada

        when(penaltyRepository.findById(penaltyId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> penaltyService.getPenaltyById(penaltyId));
    }

    @Test
    void testDeletePenalty() {
        // Arrange
        int penaltyId = 1;
        Penalty penalty = new Penalty();
        penalty.setId(penaltyId);

        when(penaltyRepository.existsById(penaltyId)).thenReturn(true);

        // Act
        penaltyService.deletePenalty(penaltyId);

        // Assert
        verify(penaltyRepository, times(1)).deleteById(penaltyId);
    }

    @Test
    void testDeletePenalty_NotFound() {
        // Arrange
        int penaltyId = 999; // Id yang tidak ada

        when(penaltyRepository.existsById(penaltyId)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> penaltyService.deletePenalty(penaltyId));
        verify(penaltyRepository, never()).deleteById(penaltyId); // Pastikan tidak terpanggil jika id tidak ditemukan
    }
}
