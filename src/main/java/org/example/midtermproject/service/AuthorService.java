package org.example.midtermproject.service;

import jakarta.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import org.example.midtermproject.dto.request.AuthorRequest;
import org.example.midtermproject.dto.response.AuthorResponse;
import org.example.midtermproject.entity.Author;
import org.example.midtermproject.entity.Book;
import org.example.midtermproject.repository.AuthorRepository;
import org.example.midtermproject.repository.BookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

  private final AuthorRepository authorRepository;
  private final BookRepository bookRepository;

  public AuthorService(AuthorRepository authorRepository, BookRepository bookRepository) {
    this.authorRepository = authorRepository;
    this.bookRepository = bookRepository;
  }

  @Transactional
  public AuthorResponse createAuthor(AuthorRequest authorRequest) {
    ensureEmailAvailable(authorRequest.email(), null);

    Author author = new Author();
    author.from(authorRequest);
    return AuthorResponse.from(authorRepository.save(author));
  }

  @Transactional
  public AuthorResponse updateAuthor(Long authorId, AuthorRequest authorRequest) {
    Author author = findAuthorWithBooks(authorId);
    ensureEmailAvailable(authorRequest.email(), authorId);

    author.from(authorRequest);
    return AuthorResponse.from(authorRepository.save(author));
  }

  @Transactional
  public void deleteAuthor(Long authorId) {
    Author author = findAuthorWithBooks(authorId);

    for (Book book : author.getBooks()) {
      book.getAuthors().remove(author);
    }
    author.getBooks().clear();

    authorRepository.delete(author);
  }

  @Transactional(readOnly = true)
  public Page<AuthorResponse> getAuthors(Pageable pageable) {
    return authorRepository.findAll(pageable).map(AuthorResponse::from);
  }

  @Transactional(readOnly = true)
  public Page<AuthorResponse> getAuthorsByLastName(String lastName, Pageable pageable) {
    return authorRepository.findByLastNameContainingIgnoreCase(lastName, pageable)
        .map(AuthorResponse::from);
  }

  @Transactional(readOnly = true)
  public AuthorResponse getAuthor(Long authorId) {
    return AuthorResponse.from(authorRepository.findById(authorId)
        .orElseThrow(() -> new NoSuchElementException("Author not found: " + authorId)));
  }

  @Transactional(readOnly = true)
  public List<Author> findAllById(Collection<Long> authorIds) {
    List<Author> foundAuthors = authorRepository.findAllById(authorIds);
    if (foundAuthors.size() != authorIds.size()) {
      throw new NoSuchElementException("One or more authors were not found");
    }
    return foundAuthors;
  }

  private Author findAuthorWithBooks(Long authorId) {
    return authorRepository.findByIdWithBook(authorId)
        .orElseThrow(() -> new NoSuchElementException("Author not found: " + authorId));
  }

  private void ensureEmailAvailable(String email, Long currentAuthorId) {
    authorRepository.findByEmailIgnoreCase(email).ifPresent(existing -> {
      if (currentAuthorId == null || existing.getId() != currentAuthorId) {
        throw new IllegalArgumentException("Email is already in use: " + email);
      }
    });
  }

  @Transactional
  public void addBookToAuthor(@Positive Long authorId, @Positive Long bookId) {
    Author author = authorRepository.findById(authorId)
        .orElseThrow(() -> new NoSuchElementException("Author not found: " + authorId));
    Book book = bookRepository.findByIdWithAuthors(bookId)
        .orElseThrow(() -> new NoSuchElementException("Book not found: " + bookId));

    boolean alreadyLinked = book.getAuthors().stream()
        .anyMatch(existingAuthor -> existingAuthor.getId() == authorId);
    if (!alreadyLinked) {
      book.getAuthors().add(author);
    }
  }
}
