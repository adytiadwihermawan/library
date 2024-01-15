package com.miniproject.library.controller;

import com.miniproject.library.dto.penalty.PenaltyRequest;
import com.miniproject.library.dto.penalty.PenaltyResponse;
import com.miniproject.library.entity.Loan;
import com.miniproject.library.entity.Penalty;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.LoanRepository;
import com.miniproject.library.service.PenaltyService;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class PenaltyControllerTest {

    @Mock
    private PenaltyService penaltyService;

    @Mock
    private LoanRepository loanRepository;

    @InjectMocks
    private PenaltyController penaltyController;
    private final ModelMapper mapper = new ModelMapper();
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    private List<Penalty> mockPenalties() {
        Penalty penalty = new Penalty();
        penalty.setAmount(50000);
        penalty.setId(1);
        penalty.setLoan(new Loan());
        List<Penalty> penalties = new ArrayList<>();
        penalties.add(penalty);
        return penalties;
    }
    private PenaltyRequest createPenaltyRequest() {
        PenaltyRequest request = new PenaltyRequest();
        request.setAmount(50000.0);
        return request;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void createPenalty_WithValidLoanIdAndAmount_ReturnsPenaltyResponse() {
        Integer loanId = 1;
        Integer amount = 50;
        Loan loan = new Loan();

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));

        PenaltyResponse mockResponse = PenaltyResponse.builder()
                .id(1)
                .amount(50)
                .loanId(1)
                .build();

        when(penaltyService.createPenalty(loan, amount)).thenReturn(mockResponse);

        ResponseEntity<PenaltyResponse> responseEntity = penaltyController.createPenalty(loanId, amount);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockResponse, responseEntity.getBody());
    }

    @Test
    void createPenalty_WithInvalidLoanIdAndAmount_ReturnsPenaltyResponse() {
        Integer loanId = 999;
        Integer amount = 50;
        Loan loan = new Loan();

        when(loanRepository.findById(loanId)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> penaltyController.createPenalty(loanId, amount));
    }

    @Test
    void getAllPenalties_ReturnsListOfPenalties() {
        List<Penalty> mockPenalties = Collections.singletonList(new Penalty());
        when(penaltyService.getAllPenalties()).thenReturn(mockPenalties);

        ResponseEntity<List<Penalty>> responseEntity = penaltyController.getAllPenalties();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(mockPenalties, responseEntity.getBody());
    }

    @Test
    void getPenaltyById_WithValidId_ReturnsPenaltyResponse() {
        Integer penaltyId = 1;
        Penalty mockPenalty = new Penalty();


        PenaltyResponse expectedResponse = PenaltyResponse.builder()
                .id(1)
                .amount(50)
                .loanId(1)
                .build();

        when(penaltyService.getPenaltyById(penaltyId)).thenReturn(expectedResponse);

        ResponseEntity<PenaltyResponse> responseEntity = penaltyController.getPenaltyById(penaltyId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedResponse, responseEntity.getBody());
    }

    @Test
    void deletePenalty() {
        Integer penaltyId = 1;

        ResponseEntity<String> responseEntity = penaltyController.deletePenalty(penaltyId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        verify(penaltyService, times(1)).deletePenalty(anyInt());
    }


}

