package io.ropereralk.github.library.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        @Schema(
                description = "Book title",
                example = "Clean Architecture"
        )
        @NotBlank
        String title,

        @Schema(
                description = "Book author",
                example = "Robert C. Martin"
        )
        @NotBlank
        String author,

        @Schema(
                description = "ISBN number",
                example = "9780134494166"
        )
        @NotBlank
        String isbn,

        @Schema(
                description = "Number of copies available",
                example = "5"
        )
        @Min(1)
        int noOfCopies) {
}
