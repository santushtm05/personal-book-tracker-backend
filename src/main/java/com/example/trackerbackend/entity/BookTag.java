package com.example.trackerbackend.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "books_tags")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookTag {

    @EmbeddedId
    private BookTagId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("bookId")
    @JoinColumn(
            name = "book_id",
            foreignKey = @ForeignKey(name = "FK_BOOK_TAG")
    )
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("tagId")
    @JoinColumn(
            name = "tag_id",
            foreignKey = @ForeignKey(name = "FK_TAG_BOOK")
    )
    private Tag tag;
}