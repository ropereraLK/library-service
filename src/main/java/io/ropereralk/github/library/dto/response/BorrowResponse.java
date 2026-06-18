package io.ropereralk.github.library.dto.response;

import lombok.Builder;
import java.time.Instant;

@Builder
public record BorrowResponse(
        Long borrowerId,
        String borrowerName,
        Long bookCopyId,
        String isbn,
        Integer bookCopyNumber,
        Instant borrowedDate
) {
}
