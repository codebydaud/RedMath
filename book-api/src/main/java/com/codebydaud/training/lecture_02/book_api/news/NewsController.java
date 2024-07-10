//package com.codebydaud.training.lecture_02.book_api.news;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.concurrent.CompletableFuture;
//
//@RestController
//public class NewsController {
//    @Autowired
//    public final NewsApiClient newsApiClient;
//    public NewsController(NewsApiClient newsApiClient) {
//        this.newsApiClient = newsApiClient;
//    }
//    @GetMapping("/json-data")
//    public CompletableFuture<String> getNews() {
//        return newsApiClient.fetchNewsFromApi();
//    }
//}
