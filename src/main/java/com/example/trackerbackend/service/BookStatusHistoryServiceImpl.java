package com.example.trackerbackend.service;

import com.example.trackerbackend.DAO.BookStatusHistoryDAO;
import com.example.trackerbackend.DTO.response.BookStatusHistoryResponseDTO;
import com.example.trackerbackend.entity.Book;
import com.example.trackerbackend.entity.BookStatus;
import com.example.trackerbackend.entity.BookStatusHistory;
import com.example.trackerbackend.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        List<BookStatusHistory> historyList =
                historyDAO.findByBookIdOrderByChangedAtDesc(bookId);

        List<BookStatusHistoryResponseDTO> responseList = new ArrayList<>();

        for (BookStatusHistory history : historyList) {
            responseList.add(mapToDTO(history));
        }

        return responseList;
    }

    @Override
    public BookStatusHistoryResponseDTO getLatestStatusChange(Integer bookId) {

        Optional<BookStatusHistory> optionalHistory =
                historyDAO.findTopByBookIdOrderByChangedAtDesc(bookId);

        if (optionalHistory.isPresent()) {
            return mapToDTO(optionalHistory.get());
        }

        throw new ResourceNotFoundException("Book status history");
    }

    private BookStatusHistoryResponseDTO mapToDTO(BookStatusHistory history) {

        String oldStatus = null;

        if (history.getOldStatus() != null) {
            oldStatus = history.getOldStatus().name();
        }

        return BookStatusHistoryResponseDTO.builder()
                .id(history.getId())
                .bookId(history.getBook().getId())
                .oldStatus(oldStatus)
                .newStatus(history.getNewStatus().name())
                .changedAt(history.getChangedAt())
                .build();
    }
}