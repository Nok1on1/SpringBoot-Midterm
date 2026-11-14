package org.example.midtermproject.controller;

import jakarta.validation.Valid;
import org.example.midtermproject.dto.request.AuthorRequest;
import org.example.midtermproject.dto.request.BookRequest;
import org.example.midtermproject.service.AuthorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping("/api/authors")
public class AuthorController {

  AuthorService authorService;

  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @PostMapping(path = "/uploadAuthor")
  public void insertAuthor(@Valid @RequestBody AuthorRequest authorReq) {
    authorService.addAuthor(authorReq);
  }
}
