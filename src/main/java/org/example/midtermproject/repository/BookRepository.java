package org.example.midtermproject.repository;

import java.util.Optional;
import org.example.midtermproject.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {

  @Query("select b from Book b join b.authors a where a.id = :authorId")
  Page<Book> findBooksByAuthorId(@Param("authorId") Long authorId, Pageable pageable);

  @Query("select distinct b from Book b left join fetch b.authors where b.id = :id")
  Optional<Book> findByIdWithAuthors(@Param("id") Long id);

  @Query("select b.coverImagePath from Book b where b.id = :id")
  Optional<String> findCoverImagePathById(@Param("id") Long id);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update Book b set b.coverImagePath = :path where b.id = :id")
  int updateCoverImagePathById(@Param("path") String path, @Param("id") Long id);
}
