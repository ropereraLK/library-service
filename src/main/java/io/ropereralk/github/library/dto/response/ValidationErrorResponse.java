package io.ropereralk.github.library.dto.response;

import java.util.Map;

public record ValidationErrorResponse(
        Map<String, String> errors
) {
}
