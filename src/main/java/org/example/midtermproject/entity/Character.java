package org.example.midtermproject.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.example.midtermproject.annotations.Sortable;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(name = "characters")
@Getter
@Setter
public class Character {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  @Sortable(type = Long.class)
  private long id;


  @Column(nullable = false)
  @Sortable(type = String.class)
  private String name;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  @Sortable(type = Role.class)
  private Role role;

  public enum Role {
    PROTAGONIST,
    ANTAGONIST,
    SUPPORTING
  }

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "book_id", nullable = false)
  @BatchSize(size = 30)
  private Book book;
}
