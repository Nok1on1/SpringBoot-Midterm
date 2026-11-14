package org.example.midtermproject.dto.request;

import jakarta.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import org.example.midtermproject.entity.Character;

public record SearchBookRequest(
    String name,

    @PastOrPresent(message = "Publish date cannot be in the future")
    LocalDate publishedAfter,

    Set<Long> authorIds,

    List<Long> characterIds,

    List<Character.Role> characterRoles
) {

}
