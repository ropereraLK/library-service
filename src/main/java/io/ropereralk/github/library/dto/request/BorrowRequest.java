package io.ropereralk.github.library.dto.request;

public record BorrowRequest (
        Long borrowerId,
        Long bookCopyId
) {
}
