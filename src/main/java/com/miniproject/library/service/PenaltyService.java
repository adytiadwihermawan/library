package com.miniproject.library.service;

import com.miniproject.library.dto.penalty.PenaltyResponse;
import com.miniproject.library.entity.Loan;
import com.miniproject.library.entity.Penalty;
import com.miniproject.library.exception.ResourceNotFoundException;
import com.miniproject.library.repository.LoanRepository;
import com.miniproject.library.repository.PenaltyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


import com.miniproject.library.repository.BookRepository;


@Service
@RequiredArgsConstructor
public class PenaltyService {
    private final PenaltyRepository penaltyRepository;
    private static final String ID_PENALTY_NOT_FOUND = "Id Penalty Not Found";

    public List<Penalty> getAllPenalties() {
        return penaltyRepository.findAll();
    }

    public PenaltyResponse createPenalty(Loan loan, Integer amount) {
        Penalty penalty = new Penalty();
        penalty.setLoan(loan);
        penalty.setAmount(amount);

        penaltyRepository.save(penalty);


        return PenaltyResponse.builder()
                .id(penalty.getId())
                .amount(penalty.getAmount())
                .loanId(penalty.getLoan().getId())
                .build();
    }

    public PenaltyResponse getPenaltyById(Integer id) {
        Penalty penalty = penaltyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ID_PENALTY_NOT_FOUND));

        return PenaltyResponse.builder()
                .id(penalty.getId())
                .amount(penalty.getAmount())
                .loanId(penalty.getLoan().getId()) // Include the associated loan ID in the response
                // Include other relevant information in the response
                .build();
    }

    public void deletePenalty(Integer id) {
        if (penaltyRepository.existsById(id)) {
            penaltyRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(ID_PENALTY_NOT_FOUND);
        }
    }

}

