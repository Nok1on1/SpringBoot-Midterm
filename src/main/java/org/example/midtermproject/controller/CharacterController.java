package org.example.midtermproject.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.util.List;
import org.example.midtermproject.dto.request.CharacterRequest;
import org.example.midtermproject.dto.response.CharacterResponse;
import org.example.midtermproject.dto.response.PageResponse;
import org.example.midtermproject.entity.Character;
import org.example.midtermproject.service.CharacterService;
import org.example.midtermproject.util.PageableUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Validated
public class CharacterController {

  private static final PageableUtils PAGEABLE = new PageableUtils(Character.class);

  private final CharacterService characterService;

  public CharacterController(CharacterService characterService) {
    this.characterService = characterService;
  }

  @PostMapping("/books/{bookId}/character")
  @ResponseStatus(HttpStatus.CREATED)
  public CharacterResponse createCharacter(@PathVariable @Positive Long bookId,
      @Valid @RequestBody CharacterRequest characterRequest) {
    return characterService.createCharacter(bookId, characterRequest);
  }

  @GetMapping("/books/{bookId}/characters")
  public PageResponse<CharacterResponse> getCharacters(
      @PathVariable @Positive Long bookId,
      @RequestParam(required = false) Character.Role role,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
      @RequestParam(defaultValue = "name") String sortBy,
      @RequestParam(defaultValue = "asc") String sortDir) {
    Pageable pageable = PAGEABLE.of(page, size, sortBy, sortDir);
    return PageResponse.from(characterService.getCharactersByBook(bookId, role, pageable));
  }

  @GetMapping("/books/{bookId}/characters/search")
  public List<CharacterResponse> searchCharacters(@PathVariable @Positive Long bookId,
      @RequestParam @Size(min = 1, max = 100) String name) {
    return characterService.searchCharactersInBook(bookId, name);
  }

  @GetMapping("/characters/{characterId}")
  public CharacterResponse getCharacter(@PathVariable @Positive Long characterId) {
    return characterService.getCharacter(characterId);
  }

  @PutMapping("/characters/{characterId}")
  public CharacterResponse updateCharacter(@PathVariable @Positive Long characterId,
      @Valid @RequestBody CharacterRequest characterRequest) {
    return characterService.updateCharacter(characterId, characterRequest);
  }

  @DeleteMapping("/characters/{characterId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteCharacter(@PathVariable @Positive Long characterId) {
    characterService.deleteCharacter(characterId);
  }
}
