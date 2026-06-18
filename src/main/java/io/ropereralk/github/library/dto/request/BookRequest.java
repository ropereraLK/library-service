package io.ropereralk.github.library.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record BookRequest(
        @NotBlank
        String title,

        @NotBlank
        String author,

        @NotBlank
        String isbn,

        @Min(value = 1, message = "A book must have at least one copy")
        int noOfCopies) {
}
