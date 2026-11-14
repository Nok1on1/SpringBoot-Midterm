package org.example.midtermproject.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import lombok.Getter;

public record AuthorRequest(

    @NotBlank(message = "First name required.")
    String firstName,

    @NotBlank(message = "Last name required.")
    String lastName,

    @Email
    String email,

    @Past
    LocalDate birthDate
) {

}
