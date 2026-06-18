package io.ropereralk.github.library.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BorrowerRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        @Size(max = 255, message = "Email cannot exceed 255 characters")
        String email
) {
}
