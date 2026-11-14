package org.example.midtermproject.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record BookRequest(
    @NotBlank(message = "Name is required")
    String name,

    @Size(max = 2500)
    String description,

    @PastOrPresent(message = "Publish date cannot be in the future")
    LocalDate publishDate,

    @NotEmpty(message = "At least one author is required")
    Set<Long> authorIds
) {

}
