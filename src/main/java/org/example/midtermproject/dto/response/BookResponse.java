package org.example.midtermproject.dto.response;

import java.time.LocalDate;
import java.util.List;
import org.example.midtermproject.entity.Author;
import org.example.midtermproject.entity.Book;

public record BookResponse(
    Long id,
    String name,
    String description,
    LocalDate publishDate,
    String coverImagePath,
    List<AuthorSummary> authors,
    List<CharacterResponse> characters
) {

  public static BookResponse from(Book book) {
    return new BookResponse(
        book.getId(),
        book.getName(),
        book.getDescription(),
        book.getPublishDate(),
        book.getCoverImagePath(),
        book.getAuthors().stream().map(AuthorSummary::from).toList(),
        book.getCharacters().stream().map(CharacterResponse::from).toList()
    );
  }

  public record AuthorSummary(Long id, String firstName, String lastName) {

    public static AuthorSummary from(Author author) {
      return new AuthorSummary(author.getId(), author.getFirstName(), author.getLastName());
    }
  }
}
