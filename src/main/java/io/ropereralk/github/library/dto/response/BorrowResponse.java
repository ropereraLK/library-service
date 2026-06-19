package io.ropereralk.github.library.dto.response;

import lombok.Builder;

import java.time.Instant;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

@Builder
@Schema(description = "Book borrowing details")
public record BorrowResponse(

        @Schema(
                description = "Unique identifier of the borrower",
                example = "1"
        )
        Long borrowerId,

        @Schema(
                description = "Name of the borrower",
                example = "John Doe"
        )
        String borrowerName,

        @Schema(
                description = "Unique identifier of the borrowed book copy",
                example = "1001"
        )
        Long bookCopyId,

        @Schema(
                description = "ISBN of the borrowed book",
                example = "9780132350884"
        )
        String isbn,

        @Schema(
                description = "Copy number of the borrowed book",
                example = "2"
        )
        Integer bookCopyNumber,

        @Schema(
                description = "Timestamp when the book was borrowed",
                example = "2026-06-19T10:30:00Z"
        )
        Instant borrowedDate

) {
}
