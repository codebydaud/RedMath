package com.codebydaud.training.lecture_02.book_api.book;

import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public Optional<Book> findById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    public List<Book> findAll(Integer page, Integer size) {
        if (page < 0) {
            page = 0;
        }
        if (size > 1000) {
            size = 1000;
        }
        return bookRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public Book create(Book book) {
        book.setBookId(System.currentTimeMillis());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        book.setAuthor(username);
        book.setPublishedAt(LocalDateTime.now());
        return bookRepository.save(book);
    }
}
