package com.example.trackerbackend.service;

import com.example.trackerbackend.DTO.response.BookStatusHistoryResponseDTO;
import com.example.trackerbackend.entity.Book;
import com.example.trackerbackend.entity.BookStatus;

import java.util.List;

public interface BookStatusHistoryService {
    void recordStatusChange(Book book, BookStatus oldStatus, BookStatus newStatus);
    List<BookStatusHistoryResponseDTO> getHistoryByBook(Integer bookId);
    BookStatusHistoryResponseDTO getLatestStatusChange(Integer bookId);
}