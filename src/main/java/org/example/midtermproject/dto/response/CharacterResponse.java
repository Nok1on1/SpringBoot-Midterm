package org.example.midtermproject.dto.response;

import org.example.midtermproject.entity.Character;

public record CharacterResponse(
    Long id,
    String name,
    Character.Role role,
    Long bookId
) {

  public static CharacterResponse from(Character character) {
    return new CharacterResponse(
        character.getId(),
        character.getName(),
        character.getRole(),
        character.getBook().getId()
    );
  }
}
