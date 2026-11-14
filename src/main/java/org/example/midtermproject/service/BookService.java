package org.example.midtermproject.service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import org.example.midtermproject.dto.request.BookRequest;
import org.example.midtermproject.dto.request.SearchBookRequest;
import org.example.midtermproject.entity.Author;
import org.example.midtermproject.entity.Book;
import org.example.midtermproject.repository.BookRepository;
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
  public Book insertBook(BookRequest bookReq) {
    List<Author> authorsFound = authorService.findAllById(bookReq.authorIds());

    Book book = new Book();
    book.setName(bookReq.name());
    book.setAuthors(authorsFound);
    book.setPublishDate(bookReq.publishDate());
    book.setDescription(bookReq.description());
    bookRepository.save(book);
    return book;
  }

  @Transactional
  public void deleteBook(Long bookId) {
    bookRepository.deleteById(bookId);
  }

  @Transactional
  public String UploadBookCoverImage(long bookId, MultipartFile file) {
    var coverImagePath = uploadImageService.store(file);
    var success = bookRepository.updateCoverImagePathById(coverImagePath, bookId);
    if (success != 1) {
      throw new RuntimeException("Failed to upload cover image");
    }

    return coverImagePath;
  }

  public Page<Book> getBooks(Pageable pageable) {
    return bookRepository.findAll(pageable);
  }

  public List<Book> searchBooks(SearchBookRequest searchBookRequest) {
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
      if (searchBookRequest.characterRoles() != null && !searchBookRequest.characterRoles()
          .isEmpty()) {
        predicates.add(root.join("characters").get("role").in(searchBookRequest.characterRoles()));
      }
      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    };

    return bookRepository.findAll(spec);
  }
}
