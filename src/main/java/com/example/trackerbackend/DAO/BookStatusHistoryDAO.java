package com.example.trackerbackend.DAO;

import com.example.trackerbackend.entity.BookStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookStatusHistoryDAO extends JpaRepository<BookStatusHistory,Integer> {
    List<BookStatusHistory> findByBookIdOrderByChangedAtDesc(Integer bookId);
    List<BookStatusHistory> findByBookId(Integer bookId);
    Optional<BookStatusHistory> findTopByBookIdOrderByChangedAtDesc(Integer bookId);
}