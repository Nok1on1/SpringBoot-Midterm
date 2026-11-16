package org.example.midtermproject.repository;

import java.util.Optional;
import org.example.midtermproject.entity.Author;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>,
    JpaSpecificationExecutor<Author> {

  Optional<Author> findByEmailIgnoreCase(String email);

  @Query("select distinct a from Author a left join fetch a.books where a.id = :id")
  Optional<Author> findByIdWithBook(@Param("id") Long authorId);

  @Override
  Page<Author> findAll(@NonNull Specification<Author> spec, @NonNull Pageable pageable);
}
