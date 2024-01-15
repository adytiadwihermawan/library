package com.miniproject.library.repository;

import com.miniproject.library.entity.BookCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCartRepository extends JpaRepository<BookCart, Integer> {
}
