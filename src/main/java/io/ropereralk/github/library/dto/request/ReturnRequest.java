package io.ropereralk.github.library.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Book return request")
public record ReturnRequest(

        @Schema(
                description = "Unique identifier of the book copy being returned",
                example = "1001"
        )
        @NotNull(message = "Book copy ID is required")
        Long bookCopyId

) {
}