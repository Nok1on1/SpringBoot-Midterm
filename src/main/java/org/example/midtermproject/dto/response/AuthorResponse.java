package org.example.midtermproject.dto.response;

import java.time.LocalDate;
import org.example.midtermproject.entity.Author;

public record AuthorResponse(
    Long id,
    String firstName,
    String lastName,
    String email,
    LocalDate birthDate
) {

  public static AuthorResponse from(Author author) {
    return new AuthorResponse(
        author.getId(),
        author.getFirstName(),
        author.getLastName(),
        author.getEmail(),
        author.getBirthDate()
    );
  }
}
