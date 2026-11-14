package org.example.midtermproject.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;
import org.example.midtermproject.dto.request.AuthorRequest;

@Entity
@Table(name = "authors")
@Getter
@Setter
public class Author {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(nullable = false)
  private String firstName;

  @Column(nullable = false)
  private String lastName;

  @Column(nullable = false, unique = true)
  private String email;

  private LocalDate birthDate;

  @ManyToMany(mappedBy = "authors")
  private List<Book> books = new ArrayList<>();


  public static Author from(AuthorRequest authorRequest) {
    Author author = new Author();
    author.firstName = authorRequest.firstName();
    author.lastName = authorRequest.lastName();
    author.email = authorRequest.email();
    author.birthDate = authorRequest.birthDate();
    return author;
  }
}
