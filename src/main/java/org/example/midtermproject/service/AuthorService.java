package org.example.midtermproject.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import lombok.Setter;
import org.example.midtermproject.dto.request.AuthorRequest;
import org.example.midtermproject.entity.Author;
import org.example.midtermproject.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService {

  private final AuthorRepository authorRepository;


  public AuthorService(AuthorRepository authorRepository) {
    this.authorRepository = authorRepository;
  }

  public List<Author> findAllById(Set<Long> authorIds) {
    var foundAuthors = authorRepository.findAllById(authorIds);
    if (foundAuthors.size() != authorIds.size()) {
      throw new NoSuchElementException("One or more authors not found");
    }
    return foundAuthors;
  }

  @Transactional
  public void addAuthor(AuthorRequest authorRequest) {
    Author author = Author.from(authorRequest);
    authorRepository.save(author);
  }
}
