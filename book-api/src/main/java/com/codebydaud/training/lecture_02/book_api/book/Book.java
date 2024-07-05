package com.codebydaud.training.lecture_02.book_api.book;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name="books")
public class Book {
    @Id
    private Long bookId;
    private String title;
    private String description;
    private String author;
    private Long year;
    private LocalDateTime publishedAt;
}
