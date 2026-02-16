package com.example.trackerbackend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class BookTagId implements Serializable {

    @Column(name = "book_id")
    private Integer bookId;

    @Column(name = "tag_id")
    private Integer tagId;
}