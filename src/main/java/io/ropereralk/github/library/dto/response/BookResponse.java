package io.ropereralk.github.library.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Book details")
public record BookResponse(
        @Schema(example = "Clean Architecture")
        String title,

        @Schema(example = "Robert C. Martin")
        String author,

        @Schema(example = "9780134494166")
        String isbn) {
}
