package com.example.trackerbackend.DAO;

import com.example.trackerbackend.entity.Book;
import com.example.trackerbackend.entity.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookDAO extends JpaRepository<Book, Integer> {
    List<Book> findByUserId(Integer userId);
    Optional<Book> findById(Integer bookId);
    List<Book> findByUserIdAndStatus(Integer userId, BookStatus status);
    List<Book> findByUserIdAndTagsContaining(Integer userId, String tag);
    @Modifying
    @Query("UPDATE Book b SET b.deletedAt = CURRENT_TIMESTAMP WHERE b.id=:id AND b.deletedAt=NULL")
    void softDelete(Integer bookId);
}