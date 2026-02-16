package com.example.trackerbackend.service;

import com.example.trackerbackend.DAO.BookStatusHistoryDAO;
import com.example.trackerbackend.DTO.response.BookStatusHistoryResponseDTO;
import com.example.trackerbackend.entity.Book;
import com.example.trackerbackend.entity.BookStatus;
import com.example.trackerbackend.entity.BookStatusHistory;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class BookStatusHistoryServiceImpl implements BookStatusHistoryService {

    private final BookStatusHistoryDAO historyDAO;

    @Override
    public void recordStatusChange(Book book, BookStatus oldStatus, BookStatus newStatus) {
        BookStatusHistory history = BookStatusHistory.builder()
                .book(book)
                .oldStatus(oldStatus)
                .newStatus(newStatus)
                .changedAt(LocalDateTime.now())
                .build();

        historyDAO.save(history);
    }

    @Override
    @Transactional
    public List<BookStatusHistoryResponseDTO> getHistoryByBook(Integer bookId) {
        return historyDAO.findByBookIdOrderByChangedAtDesc(bookId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public BookStatusHistoryResponseDTO getLatestStatusChange(Integer bookId) {
        return historyDAO.findTopByBookIdOrderByChangedAtDesc(bookId)
                .map(this::mapToDTO)
                .orElse(null);
    }

    private BookStatusHistoryResponseDTO mapToDTO(BookStatusHistory history) {
        return BookStatusHistoryResponseDTO.builder()
                .id(history.getId())
                .bookId(history.getBook().getId())
                .oldStatus(history.getOldStatus() != null ? history.getOldStatus().name() : null)
                .newStatus(history.getNewStatus().name())
                .changedAt(history.getChangedAt())
                .build();
    }
}