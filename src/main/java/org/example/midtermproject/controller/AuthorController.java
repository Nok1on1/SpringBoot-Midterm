package org.example.midtermproject.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.midtermproject.dto.request.AuthorRequest;
import org.example.midtermproject.dto.request.PaginationRequest;
import org.example.midtermproject.dto.response.AuthorResponse;
import org.example.midtermproject.dto.response.PageResponse;
import org.example.midtermproject.dto.response.SearchBookResponse;
import org.example.midtermproject.entity.Author;
import org.example.midtermproject.entity.Book;
import org.example.midtermproject.service.AuthorService;
import org.example.midtermproject.service.BookService;
import org.example.midtermproject.util.PageableUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
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

@RestController
@RequestMapping("/api/authors")
@Validated
public class AuthorController {

  private static final PageableUtils AUTHOR_PAGEABLE = new PageableUtils(Author.class);
  private static final PageableUtils BOOK_PAGEABLE = new PageableUtils(Book.class);

  private final AuthorService authorService;
  private final BookService bookService;

  public AuthorController(AuthorService authorService, BookService bookService) {
    this.authorService = authorService;
    this.bookService = bookService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public AuthorResponse createAuthor(@Valid @RequestBody AuthorRequest authorRequest) {
    return authorService.createAuthor(authorRequest);
  }

  @GetMapping
  public PageResponse<AuthorResponse> getAuthors(
      @Valid @ModelAttribute PaginationRequest pagination) {
    Pageable pageable = AUTHOR_PAGEABLE.of(pagination);
    return PageResponse.from(authorService.getAuthors(pageable));
  }

  @PostMapping("/advanced-search")
  public PageResponse<AuthorResponse> searchAuthorsByLastName(
      @RequestBody AuthorRequest authorRequest,
      @Valid @ModelAttribute PaginationRequest pagination) {
    Pageable pageable = AUTHOR_PAGEABLE.of(pagination);
    return PageResponse.from(authorService.getAuthors(authorRequest, pageable));
  }

  @GetMapping("/{authorId}")
  public AuthorResponse getAuthor(@PathVariable @Positive Long authorId) {
    return authorService.getAuthor(authorId);
  }

  @PutMapping("/{authorId}")
  public AuthorResponse updateAuthor(@PathVariable @Positive Long authorId,
      @Valid @RequestBody AuthorRequest authorRequest) {
    return authorService.updateAuthor(authorId, authorRequest);
  }

  @DeleteMapping("/{authorId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteAuthor(@PathVariable @Positive Long authorId) {
    authorService.deleteAuthor(authorId);
  }

  @GetMapping("/{authorId}/books")
  public PageResponse<SearchBookResponse> getBooksByAuthor(
      @PathVariable @Positive Long authorId,
      @Valid @ModelAttribute PaginationRequest pagination) {
    Pageable pageable = BOOK_PAGEABLE.of(pagination);
    return PageResponse.from(bookService.getBooksByAuthor(authorId, pageable));
  }

  @PutMapping("/{authorId}/book")
  public void addBookToAuthor(@PathVariable @Positive Long authorId,
      @RequestParam @Positive Long bookId) {
    authorService.addBookToAuthor(authorId, bookId);
  }
}
