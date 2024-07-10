package com.codebydaud.training.lecture_02.book_api.book;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;


@RestController
public class BookController {

    private final BookService bookService;

    BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/api/v1/books/{bookId}")
    public ResponseEntity<Book> getBookById(@PathVariable("bookId") Long bookId) {
        Optional<Book> book = bookService.findBookById(bookId);
        if (book.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book.get());
    }

    @GetMapping("/api/v1/books")
    public ResponseEntity<List<Book>> getAllBooks(@RequestParam(name = "page", defaultValue = "0") Integer page,
                                                  @RequestParam(name = "size", defaultValue = "1000") Integer size) {
        return ResponseEntity.ok(bookService.findAllBooks(page, size));
    }

    @PreAuthorize("hasAnyAuthority('admin','author')")
    @PostMapping("/api/v1/books")
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        book = bookService.createBook(book);
        return ResponseEntity.created(URI.create("/api/v1/books/" + book.getBookId())).body(book);
    }

    @PreAuthorize("hasAnyAuthority('admin','editor')")
    @PutMapping("/api/v1/books/{bookId}")
    public ResponseEntity<Book> updateBook(@PathVariable("bookId") Long bookId, @RequestBody Book book) {
        Optional<Book> saved = bookService.updateBook(bookId, book);
        if (saved.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(saved.get());
    }

    @PreAuthorize("hasAuthority('admin')")
    @DeleteMapping("/api/v1/books/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable("bookId") Long bookId) {
        boolean deleted = bookService.deleteBook(bookId);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.noContent().build();
    }
}