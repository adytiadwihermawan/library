package com.miniproject.library.controller;

import com.miniproject.library.dto.penalty.PenaltyResponse;
import com.miniproject.library.entity.Loan;
import com.miniproject.library.entity.Penalty;
import com.miniproject.library.repository.LoanRepository;
import com.miniproject.library.service.PenaltyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@Tag(name = "Penalty")
@RequestMapping("/penalty")
public class PenaltyController {
    private final PenaltyService penaltyService;
    private final LoanRepository loanRepository;

    @GetMapping("/all")
    public ResponseEntity<List<Penalty>> getAllPenalties() {
        List<Penalty> penalties = penaltyService.getAllPenalties();
        return ResponseEntity.ok(penalties);
    }

    @PostMapping
    public ResponseEntity<PenaltyResponse> createPenalty(@RequestParam Integer loanId, @RequestParam Integer amount) {
        Optional<Loan> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isPresent()) {
            Loan loan = optionalLoan.get();
            PenaltyResponse response = penaltyService.createPenalty(loan, amount);
            return ResponseEntity.ok(response);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Loan with ID " + loanId + " not found.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PenaltyResponse> getPenaltyById(@PathVariable Integer id) {
        PenaltyResponse response = penaltyService.getPenaltyById(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePenalty(@PathVariable Integer id) {
        penaltyService.deletePenalty(id);
        return ResponseEntity.ok("Penalty with ID " + id + " deleted successfully.");
    }


}
