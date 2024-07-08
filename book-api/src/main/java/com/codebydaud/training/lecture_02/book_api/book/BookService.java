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

    public Optional<Book> findBookById(Long bookId) {
        return bookRepository.findById(bookId);
    }

    public List<Book> findAllBooks(Integer page, Integer size) {
        if (page < 0) {
            page = 0;
        }
        if (size > 1000) {
            size = 1000;
        }
        return bookRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    public Book createBook(Book book) {
        book.setBookId(System.currentTimeMillis());
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        book.setAuthor(username);
        book.setPublishedAt(LocalDateTime.now());
        return bookRepository.save(book);
    }

    public Optional<Book> updateBook(Long bookId, Book book) {
        Optional<Book> existing = bookRepository.findById(bookId);
        if (existing.isPresent()) {
            existing.get().setTitle(book.getTitle());
            existing.get().setDescription(book.getDescription());
            existing = Optional.of(bookRepository.save(existing.get()));
        }
        return existing;
    }

    public boolean deleteBook(Long bookId) {
       if(bookRepository.existsById(bookId)) {
           bookRepository.deleteById(bookId);
           return true;
       }
       return false;
    }
}
