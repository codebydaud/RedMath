package com.codebydaud.training.lecture_02.news_api;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewsController {

    private final NewsRepository newsRepository;

    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    @GetMapping("/api/v1/news/{newsId}")
    public ResponseEntity<News> getNews(@PathVariable("newsId") Long newsId) {
        Optional<News> news = newsRepository.findById(newsId);
        if (news.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(news.get());
    }

    @GetMapping("/api/v1/news")
    public ResponseEntity<List<News>> getAllNews(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                          @RequestParam(name = "page", defaultValue = "1000") Integer size) {
        return ResponseEntity.ok(newsRepository.findAll());
    }

    /* @DeleteMapping("/api/v1/news/{newsId}")
    public ResponseEntity<HttpStatus> deleteNews(@PathVariable("newsId") Long newsId) {
        try {
            newsRepository.deleteById(newsId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    } */
}