package org.example.midtermproject.service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import org.example.midtermproject.dto.request.BookRequest;
import org.example.midtermproject.dto.request.SearchBookRequest;
import org.example.midtermproject.dto.response.BookResponse;
import org.example.midtermproject.dto.response.SearchBookResponse;
import org.example.midtermproject.entity.Author;
import org.example.midtermproject.entity.Book;
import org.example.midtermproject.repository.BookRepository;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BookService {

  private final BookRepository bookRepository;
  private final AuthorService authorService;
  private final IUploadImageService uploadImageService;

  public BookService(BookRepository bookRepository, AuthorService authorService,
      IUploadImageService uploadImageService) {
    this.bookRepository = bookRepository;
    this.authorService = authorService;
    this.uploadImageService = uploadImageService;
  }

  @Transactional
  public BookResponse createBook(BookRequest bookRequest) {
    List<Author> authors = authorService.findAllById(bookRequest.authorIds());

    Book book = new Book();
    applyRequest(book, bookRequest, authors);
    return BookResponse.from(bookRepository.save(book));
  }

  @Transactional
  public BookResponse updateBook(Long bookId, BookRequest bookRequest) {
    Book book = findBook(bookId);
    List<Author> authors = authorService.findAllById(bookRequest.authorIds());

    applyRequest(book, bookRequest, authors);
    return BookResponse.from(bookRepository.save(book));
  }

  @Transactional
  public void deleteBook(Long bookId) {
    Book book = findBook(bookId);
    String coverImagePath = book.getCoverImagePath();

    bookRepository.delete(book);
    uploadImageService.delete(coverImagePath);
  }

  @Transactional
  public String uploadBookCoverImage(Long bookId, MultipartFile file) {
    Book book = findBook(bookId);
    String oldImagePath = book.getCoverImagePath();
    String newImagePath = uploadImageService.store(file);

    int updatedRows = bookRepository.updateCoverImagePathById(newImagePath, bookId);
    if (updatedRows != 1) {
      uploadImageService.delete(newImagePath);
      throw new NoSuchElementException("Book not found: " + bookId);
    }

    uploadImageService.delete(oldImagePath);
    return newImagePath;
  }

  @Transactional(readOnly = true)
  public Page<SearchBookResponse> getBooks(Pageable pageable) {
    return bookRepository.findAll(pageable).map(SearchBookResponse::from);
  }

  @Transactional(readOnly = true)
  public BookResponse getBook(Long bookId) {
    Book book = bookRepository.findByIdWithAuthors(bookId)
        .orElseThrow(() -> new NoSuchElementException("Book not found: " + bookId));
    return BookResponse.from(book);
  }

  @Transactional(readOnly = true)
  public Page<SearchBookResponse> getBooksByAuthor(Long authorId, Pageable pageable) {
    Author author = new Author();
    author.setId(authorId);
    return bookRepository.findBooksByAuthors(Collections.singletonList(author), pageable)
        .map(SearchBookResponse::from);
  }

  @Transactional(readOnly = true)
  public List<SearchBookResponse> searchBooks(SearchBookRequest searchBookRequest) {
    Specification<Book> spec = (root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (searchBookRequest.name() != null && !searchBookRequest.name().isEmpty()) {
        predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
            "%" + searchBookRequest.name().toLowerCase() + "%"));
      }
      if (searchBookRequest.publishedAfter() != null) {
        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("publishDate"),
            searchBookRequest.publishedAfter()));
      }
      if (searchBookRequest.authorIds() != null && !searchBookRequest.authorIds().isEmpty()) {
        predicates.add(root.join("authors").get("id").in(searchBookRequest.authorIds()));
      }
      if (searchBookRequest.characterIds() != null && !searchBookRequest.characterIds().isEmpty()) {
        predicates.add(root.join("characters").get("id").in(searchBookRequest.characterIds()));
      }
      if (searchBookRequest.characterRoles() != null
          && !searchBookRequest.characterRoles().isEmpty()) {
        predicates.add(root.join("characters").get("role").in(searchBookRequest.characterRoles()));
      }
      query.distinct(true);
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    return bookRepository.findAll(spec).stream().map(SearchBookResponse::from).toList();
  }

  @Transactional(readOnly = true)
  public Resource loadCoverImage(Long bookId) {
    String path = bookRepository.findCoverImagePathById(bookId)
        .orElseThrow(() -> new NoSuchElementException(
            "Cover image not found for book: " + bookId));

    if (path.isBlank()) {
      throw new NoSuchElementException("Book " + bookId + " does not have a cover image");
    }
    return uploadImageService.load(path);
  }

  private void applyRequest(Book book, BookRequest bookRequest, List<Author> authors) {
    book.setName(bookRequest.name().trim());
    book.setDescription(bookRequest.description());
    book.setPublishDate(bookRequest.publishDate());

    book.getAuthors().clear();
    book.getAuthors().addAll(authors);
  }

  private Book findBook(Long bookId) {
    return bookRepository.findById(bookId)
        .orElseThrow(() -> new NoSuchElementException("Book not found: " + bookId));
  }
}
