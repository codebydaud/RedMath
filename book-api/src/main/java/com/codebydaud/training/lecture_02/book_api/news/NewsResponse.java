package com.codebydaud.training.lecture_02.book_api.news;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NewsResponse {
    List<News> news;
}
