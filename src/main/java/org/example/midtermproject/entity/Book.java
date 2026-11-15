package org.example.midtermproject.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.example.midtermproject.annotations.Sortable;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "books")
@Setter
@Getter
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Sortable(type = Long.class)
  private long id;

  @Column(nullable = false)
  @Sortable(type = String.class)
  private String name;

  @Column(length = 2500)
  private String description;

  @Column()
  @Sortable(type = LocalDate.class)
  private LocalDate publishDate;

  private String coverImagePath;


  @ManyToMany(cascade = CascadeType.PERSIST)
  @JoinTable(name = "books_authors",
      joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "author_id"))
  @BatchSize(size = 30)
  private List<Author> authors = new ArrayList<>();


  @OneToMany(mappedBy = "book", cascade = CascadeType.ALL, orphanRemoval = true)
  @BatchSize(size = 30)
  private List<Character> characters = new ArrayList<>();
}
