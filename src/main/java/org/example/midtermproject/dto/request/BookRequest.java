package org.example.midtermproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.Set;

public record BookRequest(
    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    String name,

    @Size(max = 2500, message = "Description must not exceed 2500 characters")
    String description,

    @PastOrPresent(message = "Publish date cannot be in the future")
    LocalDate publishDate,

    @NotEmpty(message = "At least one author is required")
    Set<@Positive Long> authorIds
) {

}
