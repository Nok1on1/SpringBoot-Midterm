package org.example.midtermproject.util;

import java.lang.reflect.Field;
import org.example.midtermproject.annotations.SortableFieldCache;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageableUtils {

  private final Class<?> entityClass;

  public PageableUtils(Class<?> entityClass) {
    this.entityClass = entityClass;
  }

  public Pageable of(int page, int size, String sortBy, String direction) {
    Field sortField = SortableFieldCache.getSortableField(entityClass, sortBy);

    if (sortField == null) {
      throw new IllegalArgumentException(
          "Invalid sort property '" + sortBy + "'. Allowed values: "
              + SortableFieldCache.getSortableFieldsString(entityClass));
    }

    Sort.Direction sortDirection = Sort.Direction.fromOptionalString(direction)
        .orElseThrow(() -> new IllegalArgumentException(
            "Invalid sort direction '" + direction + "'. Allowed values: asc, desc"));

    return PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
  }
}
