package com.example.trackerbackend.service;

import com.example.trackerbackend.DTO.request.book.BookCreationRequestDTO;
import com.example.trackerbackend.DTO.request.book.BookUpdationRequestDTO;
import com.example.trackerbackend.DTO.response.BookDTO;

import java.util.List;

public interface BookService {
    BookDTO createBook(BookCreationRequestDTO bookCreationRequestDTO);
    BookDTO updateBook(BookUpdationRequestDTO bookUpdationRequestDTO, Integer bookId);
    void deleteBook(Integer id);
    BookDTO getBook(Integer id);
    List<BookDTO> getBooksByStatusAndUserId(String status);
    List<BookDTO> getBooksByUserId();
    List<BookDTO> getBooksByTagAndUserId(String tag);
}