package com.codebydaud.training.lecture_02.news_api;

import java.time.LocalDateTime;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class News {
    @Id
    private Long newsId;
    private String title;
    private String details;
    private String reportedBy;
    private LocalDateTime reportedAt;
}
