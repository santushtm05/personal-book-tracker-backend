package com.example.trackerbackend.utils.conversion;

import com.example.trackerbackend.DTO.response.BookDTO;
import com.example.trackerbackend.DTO.response.TagDTO;
import com.example.trackerbackend.DTO.response.UserDTO;
import com.example.trackerbackend.entity.Book;
import com.example.trackerbackend.entity.Tag;
import com.example.trackerbackend.entity.User;

import java.util.HashSet;
import java.util.Set;

public class EntityConversionUtils {
    public static BookDTO toBookDTO(Book book){
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
                                : null
                )
                .rating(book.getRating())
                .pages(book.getPages())
                .tags(tagDTOs)
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

    public static TagDTO toTagDTO(Tag tag) {
        return TagDTO.builder()
                .id(tag.getId())
                .tagName(tag.getName())
                .build();
    }
}