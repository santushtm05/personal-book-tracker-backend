package com.example.trackerbackend.service;

import com.example.trackerbackend.DAO.BookStatusHistoryDAO;
import com.example.trackerbackend.DTO.response.BookStatusHistoryResponseDTO;
import com.example.trackerbackend.entity.Book;
import com.example.trackerbackend.entity.BookStatus;
import com.example.trackerbackend.entity.BookStatusHistory;
import com.example.trackerbackend.exception.ResourceNotFoundException;
import com.example.trackerbackend.utils.conversion.EntityConversionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import java.util.List;
import java.util.Optional;

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
    public List<BookStatusHistoryResponseDTO> getHistoryByBook(Integer bookId) {

        List<BookStatusHistory> historyList = historyDAO.findByBookIdOrderByChangedAtDesc(bookId);

        return EntityConversionUtils.toBookStatusHistoryResponseDTOs(historyList);
    }

    @Override
    public BookStatusHistoryResponseDTO getLatestStatusChange(Integer bookId) {

        Optional<BookStatusHistory> optionalHistory = historyDAO.findTopByBookIdOrderByChangedAtDesc(bookId);

        if (optionalHistory.isPresent()) {
            return EntityConversionUtils.toBookStatusHistoryResponseDTO(optionalHistory.get());
        }

        throw new ResourceNotFoundException("Book status history");
    }
}
