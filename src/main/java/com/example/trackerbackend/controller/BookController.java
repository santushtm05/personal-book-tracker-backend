package com.example.trackerbackend.controller;

import com.example.trackerbackend.DTO.request.book.BookCreationRequestDTO;
import com.example.trackerbackend.DTO.request.book.BookUpdationRequestDTO;
import com.example.trackerbackend.DTO.response.APIResponse;
import com.example.trackerbackend.DTO.response.BookDTO;
import com.example.trackerbackend.entity.principal.CustomUserDetails;
import com.example.trackerbackend.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/")
    public ResponseEntity<APIResponse<List<BookDTO>>> getAllBooks() {
        List<BookDTO> books = bookService.getBooksByUserId();
        APIResponse<List<BookDTO>> response = APIResponse.<List<BookDTO>>builder()
                .data(books)
                .success(true)
                .message(null)
                .timestamp(LocalDateTime.now())
                .error(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<BookDTO>> getBookById(@PathVariable Integer id) {
        BookDTO bookDB = this.bookService.getBook(id);
        APIResponse<BookDTO> response = APIResponse.<BookDTO>builder()
                .success(true)
                .error(null)
                .message(null)
                .data(bookDB)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<APIResponse<BookDTO>> updateBook(@PathVariable Integer id, @Valid @RequestBody BookUpdationRequestDTO bookUpdationRequestDTO) {
        BookDTO updatedBook = this.bookService.updateBook(bookUpdationRequestDTO, id);
        APIResponse<BookDTO> response = APIResponse.<BookDTO>builder()
                .success(true)
                .error(null)
                .message("Book Updated Successfully!")
                .data(updatedBook)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/create")
    public ResponseEntity<APIResponse<BookDTO>> createBook(@Valid @RequestBody BookCreationRequestDTO bookCreationRequestDTO) {
        BookDTO createdBook = this.bookService.createBook(bookCreationRequestDTO);
        APIResponse<BookDTO> response = APIResponse.<BookDTO>builder()
                .success(true)
                .error(null)
                .message("Book Created!")
                .data(createdBook)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<APIResponse<String>> deleteBook(@PathVariable Integer id) {
        this.bookService.deleteBook(id);
        APIResponse<String> response = APIResponse.<String>builder()
                .success(true)
                .error(null)
                .message("Book Deleted!")
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    @GetMapping("/status/")
    public ResponseEntity<APIResponse<List<BookDTO>>> getBooksByStatus(@RequestParam String status) {
        List<BookDTO> books = this.bookService.getBooksByStatusAndUserId(status);
        APIResponse<List<BookDTO>> response = APIResponse.<List<BookDTO>>builder()
                .data(books)
                .success(true)
                .message(null)
                .timestamp(LocalDateTime.now())
                .error(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/search/")
    public ResponseEntity<APIResponse<List<BookDTO>>> getBooksBySearch(@RequestParam String query) {
        List<BookDTO> books = this.bookService.searchBooks(query);
        APIResponse<List<BookDTO>> response = APIResponse.<List<BookDTO>>builder()
                .data(books)
                .success(true)
                .message(null)
                .timestamp(LocalDateTime.now())
                .error(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}