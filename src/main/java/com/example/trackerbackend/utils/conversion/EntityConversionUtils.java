package com.example.trackerbackend.utils.conversion;

import com.example.trackerbackend.DTO.response.BookDTO;
import com.example.trackerbackend.DTO.response.BookStatusHistoryResponseDTO;
import com.example.trackerbackend.DTO.response.TagDTO;
import com.example.trackerbackend.DTO.response.UserDTO;
import com.example.trackerbackend.entity.Book;
import com.example.trackerbackend.entity.BookStatusHistory;
import com.example.trackerbackend.entity.Tag;
import com.example.trackerbackend.entity.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EntityConversionUtils {
    public static BookDTO toBookDTO(Book book) {
        if (book == null) {
            return null;
        }

        Set<TagDTO> tagDTOs = null;

        if (book.getTags() != null) {
            tagDTOs = new HashSet<>();
            for (Tag tag : book.getTags()) {
                tagDTOs.add(toTagDTO(tag));
            }
        }

        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .status(
                        book.getStatus() != null
                                ? book.getStatus().name()
                                : null)
                .rating(book.getRating())
                .pages(book.getPages())
                .tags(tagDTOs)
                .thumbnailUrl(book.getThumbnailUrl())
                .completedAt(book.getCompletedAt())
                .build();
    }

    public static List<BookDTO> toBookDTOs(List<Book> books) {
        if (books == null) {
            return new ArrayList<>();
        }
        List<BookDTO> bookDTOs = new ArrayList<>();
        for (Book book : books) {
            bookDTOs.add(toBookDTO(book));
        }
        return bookDTOs;
    }

    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .build();
    }

    public static TagDTO toTagDTO(Tag tag) {
        return TagDTO.builder()
                .id(tag.getId())
                .tagName(tag.getName())
                .build();
    }

    public static List<TagDTO> toTagDTOs(List<Tag> tags) {
        if (tags == null) {
            return new ArrayList<>();
        }
        List<TagDTO> tagDTOs = new ArrayList<>();
        for (Tag tag : tags) {
            tagDTOs.add(toTagDTO(tag));
        }
        return tagDTOs;
    }

    public static BookStatusHistoryResponseDTO toBookStatusHistoryResponseDTO(BookStatusHistory history) {
        if (history == null) {
            return null;
        }
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

    public static List<BookStatusHistoryResponseDTO> toBookStatusHistoryResponseDTOs(
            List<BookStatusHistory> histories) {
        if (histories == null) {
            return new ArrayList<>();
        }
        List<BookStatusHistoryResponseDTO> responseDTOs = new ArrayList<>();
        for (BookStatusHistory history : histories) {
            responseDTOs.add(toBookStatusHistoryResponseDTO(history));
        }
        return responseDTOs;
    }
}