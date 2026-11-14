package org.example.midtermproject.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.example.midtermproject.dto.request.BookRequest;
import org.example.midtermproject.dto.request.SearchBookRequest;
import org.example.midtermproject.dto.response.SearchBookResponse;
import org.example.midtermproject.entity.Book;
import org.example.midtermproject.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController

@RequestMapping("/api/books")
public class BookController {

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping(path = "/uploadBook")
  public void insertBook(@Valid @RequestBody BookRequest bookReq) {
    bookService.insertBook(bookReq);
  }

  @DeleteMapping(path = "/deleteBook/{bookId}")
  public void deleteBook(@PathVariable Long bookId) {
    bookService.deleteBook(bookId);
  }

  @GetMapping(path = "")
  public List<SearchBookResponse> getBooks(Pageable pageable) {
    List<Book> books = bookService.getBooks(pageable).getContent();
    return books.stream().map(SearchBookResponse::from).toList();
  }

  @PostMapping(path = "/search")
  public List<SearchBookResponse> searchBooks(
      @Valid @RequestBody SearchBookRequest searchBookRequest) {
    List<Book> books = bookService.searchBooks(searchBookRequest);
    return books.stream().map(SearchBookResponse::from).toList();
  }

  @PutMapping(path = "/uploadCover/{bookId}")
  public String uploadCoverImage(@PathVariable Long bookId, @RequestParam MultipartFile file) {
    return bookService.UploadBookCoverImage(bookId, file);
  }

}
