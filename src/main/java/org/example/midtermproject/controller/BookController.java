package org.example.midtermproject.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import java.util.Map;
import org.example.midtermproject.dto.request.BookRequest;
import org.example.midtermproject.dto.request.PaginationRequest;
import org.example.midtermproject.dto.request.SearchBookRequest;
import org.example.midtermproject.dto.response.BookResponse;
import org.example.midtermproject.dto.response.PageResponse;
import org.example.midtermproject.dto.response.SearchBookResponse;
import org.example.midtermproject.entity.Book;
import org.example.midtermproject.service.BookService;
import org.example.midtermproject.util.PageableUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/books")
@Validated
public class BookController {

  private static final PageableUtils PAGEABLE = new PageableUtils(Book.class);

  private final BookService bookService;

  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public BookResponse createBook(@Valid @RequestBody BookRequest bookRequest) {
    return bookService.createBook(bookRequest);
  }

  @GetMapping
  public PageResponse<SearchBookResponse> getBooks(
      @Valid @ModelAttribute PaginationRequest pagination) {
    Pageable pageable = PAGEABLE.of(pagination);
    return PageResponse.from(bookService.getBooks(pageable));
  }

  @PostMapping("/advanced-search")
  public List<SearchBookResponse> advancedSearch(
      @Valid @RequestBody SearchBookRequest searchBookRequest) {
    return bookService.searchBooks(searchBookRequest);
  }

  @GetMapping("/{bookId}")
  public BookResponse getBook(@PathVariable @Positive Long bookId) {
    return bookService.getBook(bookId);
  }

  @PutMapping("/{bookId}")
  public BookResponse updateBook(@PathVariable @Positive Long bookId,
      @Valid @RequestBody BookRequest bookRequest) {
    return bookService.updateBook(bookId, bookRequest);
  }

  @DeleteMapping("/{bookId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteBook(@PathVariable @Positive Long bookId) {
    bookService.deleteBook(bookId);
  }

  @PostMapping(path = "/{bookId}/cover", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public Map<String, String> uploadCoverImage(@PathVariable @Positive Long bookId,
      @RequestParam("image") MultipartFile image) {
    String imagePath = bookService.uploadBookCoverImage(bookId, image);
    return Map.of("coverImagePath", imagePath);
  }

  @GetMapping("/{bookId}/cover")
  public ResponseEntity<Resource> getCoverImage(@PathVariable @Positive Long bookId) {
    Resource resource = bookService.loadCoverImage(bookId);
    MediaType mediaType = MediaTypeFactory.getMediaType(resource)
        .orElse(MediaType.APPLICATION_OCTET_STREAM);
    return ResponseEntity.ok().contentType(mediaType).body(resource);
  }
}
