package org.example.midtermproject.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaginationRequest {

  @Min(0)
  private int page = 0;

  @Min(1)
  @Max(200)
  private int size = 10;

  private String sortBy = "id";
  private String sortDir = "asc";
}
