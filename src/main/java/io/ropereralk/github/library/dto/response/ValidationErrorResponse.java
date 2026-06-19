package io.ropereralk.github.library.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Validation error response")
public record ValidationErrorResponse(
        Map<String, String> errors
) {
}
