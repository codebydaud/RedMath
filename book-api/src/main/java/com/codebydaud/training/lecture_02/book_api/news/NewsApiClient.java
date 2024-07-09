package com.codebydaud.training.lecture_02.book_api.news;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class NewsApiClient {

    private final RestTemplate restTemplate;

    public NewsApiClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    //@Cacheable(cacheNames = "news")
    @Async
    public CompletableFuture<String> fetchNewsFromApi() {
//        String apiUrl = "https://newsapi.org/v2/everything?q=tesla&apiKey=31ca192166dc40288e6e3bf7454c981d";
//        NewsResponse response = restTemplate.getForObject(apiUrl, NewsResponse.class);
//        if(response==null)
//        {
//            return CompletableFuture.completedFuture(List.of());
//        }
//        return CompletableFuture.completedFuture(response.getNews());

        String result = restTemplate
                .getForEntity("https://newsapi.org/v2/everything?q=tesla&apiKey=31ca192166dc40288e6e3bf7454c981d", String.class).getBody();
        return CompletableFuture.completedFuture(result);
    }

    @Scheduled(fixedRate = 10000)
    public void fetchNewsFromApiAsync() {
        fetchNewsFromApi();
    }

}