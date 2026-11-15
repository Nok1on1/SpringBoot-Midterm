package org.example.midtermproject.repository;

import java.util.List;
import org.example.midtermproject.entity.Character;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

  Page<Character> findByBookId(Long bookId, Pageable pageable);

  Page<Character> findByBookIdAndRole(Long bookId, Character.Role role, Pageable pageable);

  List<Character> findByBookIdAndNameContainingIgnoreCase(Long bookId, String name);

  boolean existsByBookIdAndNameIgnoreCase(Long bookId, String name);
}
