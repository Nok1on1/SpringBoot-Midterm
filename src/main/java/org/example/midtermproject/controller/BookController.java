package org.example.midtermproject.controller;

import jakarta.validation.Valid;
import java.util.List;
import org.example.midtermproject.dto.request.BookRequest;
import org.example.midtermproject.dto.request.SearchBookRequest;
import org.example.midtermproject.dto.response.SearchBookResponse;
import org.example.midtermproject.entity.Book;
import org.example.midtermproject.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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

  @PostMapping
  public void insertBook(@Valid @RequestBody BookRequest bookReq) {
    bookService.insertBook(bookReq);
  }

  @DeleteMapping("/{bookId}")
  public void deleteBook(@PathVariable Long bookId) {
    bookService.deleteBook(bookId);
  }

  @GetMapping
  public List<SearchBookResponse> getBooks(Pageable pageable) {
    List<Book> books = bookService.getBooks(pageable).getContent();
    return books.stream().map(SearchBookResponse::from).toList();
  }

  @PostMapping("/search")
  public List<SearchBookResponse> searchBooks(
      @Valid @RequestBody SearchBookRequest searchBookRequest) {
    List<Book> books = bookService.searchBooks(searchBookRequest);
    return books.stream().map(SearchBookResponse::from).toList();
  }

  @PutMapping(path = "/{bookId}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String uploadCoverImage(@PathVariable Long bookId,
      @RequestParam("image") MultipartFile file) {
    return bookService.uploadBookCoverImage(bookId, file);
  }

  @GetMapping(path = "/{bookId}/cover", produces = MediaType.IMAGE_JPEG_VALUE)
  public byte[] getCoverImage(@PathVariable Long bookId) {
    byte[] file = bookService.getBookCoverImage(bookId);
    return file != null ? file : new byte[0];
  }
}
