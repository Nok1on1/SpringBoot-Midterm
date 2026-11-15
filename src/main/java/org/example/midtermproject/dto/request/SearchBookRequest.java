package org.example.midtermproject.dto.request;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.example.midtermproject.entity.Character;

public record SearchBookRequest(
    @Size(max = 255, message = "Name must not exceed 255 characters")
    String name,

    @PastOrPresent(message = "Publish date cannot be in the future")
    LocalDate publishedAfter,

    Set<Long> authorIds,

    List<Long> characterIds,

    List<Character.Role> characterRoles
) {

}
