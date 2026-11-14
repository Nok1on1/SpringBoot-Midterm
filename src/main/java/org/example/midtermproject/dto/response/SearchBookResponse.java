package org.example.midtermproject.dto.response;

import java.util.List;
import org.example.midtermproject.entity.Book;
import org.example.midtermproject.entity.Character;

public record SearchBookResponse(
    String name,
    String description,
    String coverImagePath,
    List<String> authors,
    List<String> characters
) {

  public static SearchBookResponse from(Book book) {
    return new SearchBookResponse(
        book.getName(),
        book.getDescription(),
        book.getCoverImagePath(),
        book.getAuthors().stream().map(author -> author.getFirstName() + " " + author.getLastName())
            .toList(),
        book.getCharacters().stream().map(Character::getName)
            .toList()
    );
  }
}
