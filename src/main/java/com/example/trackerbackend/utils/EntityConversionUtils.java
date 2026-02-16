package com.example.trackerbackend.utils;

import com.example.trackerbackend.DTO.response.BookDTO;
import com.example.trackerbackend.DTO.response.UserDTO;
import com.example.trackerbackend.entity.Book;
import com.example.trackerbackend.entity.User;

public class EntityConversionUtils {
    public static BookDTO toBookDTO(Book book){
        if (book == null) {
            return null;
        }

        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .status(
                        book.getStatus() != null
                                ? book.getStatus().name()
                                : null
                )
                .rating(book.getRating())
                .pages(book.getPages())
                .thumbnailUrl(book.getThumbnailUrl())
                .completedAt(book.getCompletedAt())
                .build();
    }

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .build();
    }
}