package io.ropereralk.github.library.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Book borrowing request")
public record BorrowRequest(

        @Schema(
                description = "Unique identifier of the borrower",
                example = "1"
        )
        @NotNull(message = "Borrower ID is required")
        Long borrowerId,

        @Schema(
                description = "Unique identifier of the book copy to borrow",
                example = "1001"
        )
        @NotNull(message = "Book copy ID is required")
        Long bookCopyId


) {
}
