package com.example.trackerbackend.service;

import com.example.trackerbackend.DAO.BookDAO;
import com.example.trackerbackend.DAO.TagDAO;
import com.example.trackerbackend.DAO.UserDAO;
import com.example.trackerbackend.DTO.request.book.BookCreationRequestDTO;
import com.example.trackerbackend.DTO.request.book.BookUpdationRequestDTO;
import com.example.trackerbackend.DTO.response.BookDTO;
import com.example.trackerbackend.entity.Book;
import com.example.trackerbackend.entity.BookStatus;
import com.example.trackerbackend.entity.Tag;
import com.example.trackerbackend.entity.User;
import com.example.trackerbackend.entity.principal.CustomUserDetails;
import com.example.trackerbackend.exception.ResourceNotFoundException;
import com.example.trackerbackend.exception.ValidationException;
import com.example.trackerbackend.utils.conversion.EntityConversionUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;
    private final UserDAO userDAO;
    private final BookStatusHistoryService historyService;
    private final TagDAO tagDAO;

    private Integer getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails =  (CustomUserDetails) authentication.getPrincipal();
        return userDetails.getId();
    }

    private Book getUserOwnedBook(Integer bookId, Integer userId) {
        Book book = bookDAO.findByIdAndUserIdAndDeletedAtIsNull(bookId, userId).orElseThrow(() -> new ResourceNotFoundException("Book"));
        return book;
    }

    private BookStatus applyBookUpdates(Book book, BookUpdationRequestDTO request) {

        BookStatus oldStatus = book.getStatus();

        if (request.getTitle() != null)
            book.setTitle(request.getTitle());
        if (request.getAuthor() != null)
            book.setAuthor(request.getAuthor());
        if (request.getStatus() != null)
            book.setStatus(BookStatus.valueOf(request.getStatus()));
        if (request.getRating() != null) {
            if (request.getRating() < 0 || request.getRating() > 5)
                throw new ValidationException("Rating must be between 0 and 5");
            book.setRating(request.getRating());
        }
        if (request.getPages() != null) {
            if (request.getPages() <= 0)
                throw new ValidationException("Pages must be greater than 0");
            book.setPages(request.getPages());
        }
        if (request.getThumbnailUrl() != null)
            book.setThumbnailUrl(request.getThumbnailUrl());
        if (request.getCompletedAt() != null)
            book.setCompletedAt(request.getCompletedAt());
        if(request.getTagIds() != null) {
            Set<Tag> tags = new HashSet<>();
            for(int tagId : request.getTagIds()) {
                tags.add(tagDAO.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag")));
            }
            book.setTags(tags);
        }

        return oldStatus;
    }

    @Override
    @Transactional
    public BookDTO createBook(BookCreationRequestDTO request) {
        //check for blank book name and throw exception
        if(request.getTitle().isEmpty() || request.getAuthor().isEmpty()) throw new ValidationException("All fields are required!");

        Integer userId = getAuthenticatedUserId();
        User userDB = userDAO.findByIdAndDeletedAtIsNull(userId).orElseThrow(() -> new ResourceNotFoundException("User"));
        Book book = Book.builder()
                .id(null)
                .title(request.getTitle())
                .author(request.getAuthor())
                .status(BookStatus.valueOf(request.getStatus()))
                .rating(request.getRating())
                .pages(request.getPages())
                .thumbnailUrl(request.getThumbnailUrl())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .user(userDB)
                .build();

        // Set Tags for book
        Set<Tag> tags = new HashSet<>();
        for(int tagId : request.getTagIds()){
            Tag tag = tagDAO.findById(tagId).orElseThrow(() -> new ResourceNotFoundException("Tag"));
            tags.add(tag);
        }
        book.setTags(tags);
        //saving book to DB
        book = bookDAO.save(book);
        // Record initial status
        historyService.recordStatusChange(book, BookStatus.CREATED, book.getStatus());

        return EntityConversionUtils.toBookDTO(book);
    }

    @Override
    @Transactional
    public BookDTO updateBook(BookUpdationRequestDTO request, Integer bookId) {
        if (bookId == null)
            throw new ValidationException("Book id is required");

        Integer userId = getAuthenticatedUserId();
        Book book = getUserOwnedBook(bookId, userId);
        BookStatus oldStatus = applyBookUpdates(book, request);
        book = bookDAO.save(book);

        // Record status change
        if (request.getStatus() != null &&
                !oldStatus.equals(book.getStatus())) {

            historyService.recordStatusChange(
                    book,
                    oldStatus,
                    book.getStatus()
            );
        }

        return EntityConversionUtils.toBookDTO(book);
    }

    @Override
    @Transactional
    public void deleteBook(Integer id) {
        Integer userId = getAuthenticatedUserId();
        Book book = getUserOwnedBook(id, userId);
        book.setDeletedAt(LocalDateTime.now());
        bookDAO.save(book);
    }

    @Override
    public BookDTO getBook(Integer id) {
        Integer userId = getAuthenticatedUserId();
        Book book = getUserOwnedBook(id, userId);
        return EntityConversionUtils.toBookDTO(book);
    }

    @Override
    public List<BookDTO> getBooksByStatusAndUserId(String status) {

        Integer userId = getAuthenticatedUserId();

        BookStatus bookStatus;
        try {
            bookStatus = BookStatus.valueOf(status);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Invalid book status");
        }
        List<Book> booksFromDB =
                bookDAO.findByUserIdAndStatusAndDeletedAtIsNull(userId, bookStatus);
        if (booksFromDB.isEmpty())
            return  new  ArrayList<>();
        List<BookDTO> result = new ArrayList<>();
        for (Book book : booksFromDB)
            result.add(EntityConversionUtils.toBookDTO(book));

        return result;
    }


    @Override
    public List<BookDTO> getBooksByUserId(int page, int size) {

        Integer userId = getAuthenticatedUserId();

        Pageable pageable = PageRequest.of(page, size);
        Page<Book> booksPage =
                bookDAO.findByUserIdAndDeletedAtIsNull(userId, pageable);

        List<BookDTO> result = new ArrayList<>();
        for (Book book : booksPage.getContent())
            result.add(EntityConversionUtils.toBookDTO(book));

        return result;
    }

    public List<BookDTO> getBooksByUserId() {

        Integer userId = getAuthenticatedUserId();

        List<Book> booksFromDB =
                bookDAO.findByUserIdAndDeletedAtIsNull(userId);

        if (booksFromDB.isEmpty())
            return new   ArrayList<>();

        List<BookDTO> result = new ArrayList<>();

        for (Book book : booksFromDB)
            result.add(EntityConversionUtils.toBookDTO(book));

        return result;
    }


    @Override
    public List<BookDTO> getBooksByTagAndUserId(String tag) {

        Integer userId = getAuthenticatedUserId();

        List<Book> booksFromDB =
                bookDAO.findByUserIdAndTagsContainingAndDeletedAtIsNull(userId, tag);

        if (booksFromDB.isEmpty())
            return new ArrayList<>();

        List<BookDTO> result = new ArrayList<>();

        for (Book book : booksFromDB)
            result.add(EntityConversionUtils.toBookDTO(book));

        return result;
    }

    @Override
    public List<BookDTO> searchBooks(String query, int page, int size) {
        Integer userId = getAuthenticatedUserId();

        Pageable pageable = PageRequest.of(page, size);

        Page<Book> booksPage =
                bookDAO.searchBooks(userId, query, pageable);

        List<BookDTO> result = new ArrayList<>();

        for (Book book : booksPage.getContent())
            result.add(EntityConversionUtils.toBookDTO(book));

        return result;
    }
}