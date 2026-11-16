package org.example.midtermproject.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.example.midtermproject.dto.request.CharacterRequest;
import org.example.midtermproject.dto.request.PaginationRequest;
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
import org.springframework.web.bind.annotation.ModelAttribute;
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
      @Valid @ModelAttribute PaginationRequest pagination) {
    Pageable pageable = PAGEABLE.of(pagination);
    return PageResponse.from(characterService.getCharactersByBook(bookId, role, pageable));
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
