package org.example.midtermproject.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.example.midtermproject.dto.request.CharacterRequest;
import org.example.midtermproject.dto.response.CharacterResponse;
import org.example.midtermproject.entity.Book;
import org.example.midtermproject.entity.Character;
import org.example.midtermproject.repository.BookRepository;
import org.example.midtermproject.repository.CharacterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CharacterService {

  private final CharacterRepository characterRepository;
  private final BookRepository bookRepository;

  public CharacterService(CharacterRepository characterRepository,
      BookRepository bookRepository) {
    this.characterRepository = characterRepository;
    this.bookRepository = bookRepository;
  }

  @Transactional
  public CharacterResponse createCharacter(Long bookId, CharacterRequest characterRequest) {
    Book book = bookRepository.findById(bookId)
        .orElseThrow(() -> new NoSuchElementException("Book not found: " + bookId));
    String name = characterRequest.name().trim();

    if (characterRepository.existsByBookIdAndNameIgnoreCase(bookId, name)) {
      throw new IllegalArgumentException(
          "Character with name '" + name + "' already exists in this book");
    }

    Character character = new Character();
    character.setName(name);
    character.setRole(characterRequest.role());
    character.setBook(book);

    return CharacterResponse.from(characterRepository.save(character));
  }

  @Transactional
  public CharacterResponse updateCharacter(Long characterId, CharacterRequest characterRequest) {
    Character character = findCharacter(characterId);
    String newName = characterRequest.name().trim();

    if (!character.getName().equalsIgnoreCase(newName)
        && characterRepository.existsByBookIdAndNameIgnoreCase(character.getBook().getId(),
        newName)) {
      throw new IllegalArgumentException(
          "Character with name '" + newName + "' already exists in this book");
    }

    character.setName(newName);
    character.setRole(characterRequest.role());
    return CharacterResponse.from(characterRepository.save(character));
  }

  @Transactional
  public void deleteCharacter(Long characterId) {
    Character character = findCharacter(characterId);
    characterRepository.delete(character);
  }

  @Transactional(readOnly = true)
  public CharacterResponse getCharacter(Long characterId) {
    return CharacterResponse.from(findCharacter(characterId));
  }

  @Transactional(readOnly = true)
  public Page<CharacterResponse> getCharactersByBook(Long bookId, Character.Role role,
      Pageable pageable) {
    if (!bookRepository.existsById(bookId)) {
      throw new NoSuchElementException("Book not found: " + bookId);
    }

    Page<Character> characters = role == null
        ? characterRepository.findByBookId(bookId, pageable)
        : characterRepository.findByBookIdAndRole(bookId, role, pageable);

    return characters.map(CharacterResponse::from);
  }

  @Transactional(readOnly = true)
  public List<CharacterResponse> searchCharactersInBook(Long bookId, String name) {
    return characterRepository.findByBookIdAndNameContainingIgnoreCase(bookId, name).stream()
        .map(CharacterResponse::from)
        .toList();
  }

  private Character findCharacter(Long characterId) {
    return characterRepository.findById(characterId)
        .orElseThrow(() -> new NoSuchElementException("Character not found: " + characterId));
  }
}
