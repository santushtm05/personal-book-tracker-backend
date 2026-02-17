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
    List<Book> findByUserIdAndDeletedAtIsNull(Integer userId);
    Optional<Book> findByIdAndUserIdAndDeletedAtIsNull(Integer bookId, Integer userId);
    List<Book> findByUserIdAndStatusAndDeletedAtIsNull(Integer userId, BookStatus status);
    List<Book> findByUserIdAndTagsContainingAndDeletedAtIsNull(Integer userId, String tag);
    @Modifying
    @Query("UPDATE Book b SET b.deletedAt = CURRENT_TIMESTAMP WHERE b.id=:id AND b.deletedAt=NULL")
    void softDelete(Integer bookId);
    @Query("""
        SELECT b FROM Book b
        WHERE b.user.id = :userId
        AND (
            LOWER(b.title) LIKE LOWER(CONCAT('%', :query, '%'))
            OR LOWER(b.author) LIKE LOWER(CONCAT('%', :query, '%'))
        ) AND b.deletedAt IS NULL
    """)
    List<Book> searchBooks(Integer userId, String query);
}