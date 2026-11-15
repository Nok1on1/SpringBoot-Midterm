package org.example.midtermproject.service;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import org.example.midtermproject.dto.request.AuthorRequest;
import org.example.midtermproject.dto.response.AuthorResponse;
import org.example.midtermproject.entity.Author;
import org.example.midtermproject.entity.Book;
import org.example.midtermproject.repository.AuthorRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

  private final AuthorRepository authorRepository;

  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  @Transactional
  public AuthorResponse createAuthor(AuthorRequest authorRequest) {
    ensureEmailAvailable(authorRequest.email(), null);

    Author author = new Author();
    applyRequest(author, authorRequest);
    return AuthorResponse.from(authorRepository.save(author));
  }

  @Transactional
  public AuthorResponse updateAuthor(Long authorId, AuthorRequest authorRequest) {
    Author author = findAuthorWithBooks(authorId);
    ensureEmailAvailable(authorRequest.email(), authorId);

    applyRequest(author, authorRequest);
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

  private void applyRequest(Author author, AuthorRequest authorRequest) {
    author.setFirstName(authorRequest.firstName().trim());
    author.setLastName(authorRequest.lastName().trim());
    author.setEmail(authorRequest.email().trim());
    author.setBirthDate(authorRequest.birthDate());
  }

  private void ensureEmailAvailable(String email, Long currentAuthorId) {
    authorRepository.findByEmailIgnoreCase(email).ifPresent(existing -> {
      if (currentAuthorId == null || existing.getId() != currentAuthorId) {
        throw new IllegalArgumentException("Email is already in use: " + email);
      }
    });
  }
}
