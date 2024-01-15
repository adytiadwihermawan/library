package com.miniproject.library.repository;

import com.miniproject.library.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanRepository extends JpaRepository<Loan,Integer> {
    @Query("SELECT l.id FROM Loan l " +
            "JOIN l.bookCarts b " +
            "JOIN b.anggota a " +
            "WHERE a.id = :anggotaId " + 
            "AND l.dateReturn IS NULL " +
            "ORDER BY l.dateBorrow DESC")
    Optional<Loan> findLoanAnggota(@Param("anggotaId") Integer anggotaId);
}
