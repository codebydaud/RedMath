package com.codebydaud.training.lecture_01.hello_world;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.*;

import java.util.Map;

@RestController
public class HelloWorldController {
    private static final String template = "Hello %s!";

    @GetMapping("/api/v1/hello-world")
    public String showHelloWorld(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format(template, name);
    }

    /*Date date = new Date();
    @GetMapping("/api/v1/hello-world")
    public ResponseEntity<Map<String, String>> getJsonData() {
        Map<String, String> jsonData = new HashMap<>();
        jsonData.put("message", "Hello World!");
        jsonData.put("createdAt", date.toString());
        return ResponseEntity.ok(jsonData);
    }*/

    /* @GetMapping("/api/v1/hello-world")
    public ResponseEntity<Map<String, Object>> helloWorld() {
        return ResponseEntity.ok(Map.of("message", "Hello World!", "at", LocalDateTime.now()));
    } */
}