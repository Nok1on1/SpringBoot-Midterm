package org.example.midtermproject.repository;

import java.util.List;
import org.example.midtermproject.entity.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SearchResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book>,
    PagingAndSortingRepository<Book, Long> {

  List<Book> findByNameContainsIgnoreCase(String name);

  @Transactional
  @Modifying
  @Query("update Book b set b.coverImagePath = ?1 where b.id = ?2")
  int updateCoverImagePathById(String coverImagePath, long id);

  List<Book> findBooksByName(String name);
}
