package io.ropereralk.github.library.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Borrower details")
public record BorrowerResponse(

        @Schema(
                description = "Unique identifier of the borrower",
                example = "1"
        )
        long id,

        @Schema(
                description = "Full name of the borrower",
                example = "John Doe"
        )
        String name,

        @Schema(
                description = "Email address of the borrower",
                example = "john.doe@example.com"
        )
        String email

) {
}