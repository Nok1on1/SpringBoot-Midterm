package org.example.midtermproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.example.midtermproject.annotations.Sortable;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "authors")
@Getter
@Setter
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Sortable(type = Long.class)
  private long id;

  @Column(nullable = false)
  @Sortable(type = String.class)
  private String firstName;

  @Column(nullable = false)
  @Sortable(type = String.class)
  private String lastName;

  @Column(nullable = false, unique = true)
  @Sortable(type = String.class)
  private String email;

  @Sortable(type = LocalDate.class)
  private LocalDate birthDate;

  @ManyToMany(mappedBy = "authors")
  @BatchSize(size = 30)
  private List<Book> books = new ArrayList<>();
}
