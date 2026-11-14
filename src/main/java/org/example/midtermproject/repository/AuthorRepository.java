package org.example.midtermproject.repository;

import java.util.List;
import org.example.midtermproject.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {

  @Override
  List<Author> findAllById(Iterable<Long> longs);
}