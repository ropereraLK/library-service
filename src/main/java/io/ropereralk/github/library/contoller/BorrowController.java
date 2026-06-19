package io.ropereralk.github.library.contoller;


import io.ropereralk.github.library.dto.request.BorrowRequest;
import io.ropereralk.github.library.dto.request.ReturnRequest;
import io.ropereralk.github.library.dto.response.BorrowResponse;
import io.ropereralk.github.library.service.BorrowService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;


@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/borrow")
@Tag(
        name = "Borrowing",
        description = "Operations related to borrowing and returning books"
)
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping
    @Operation(
            summary = "Borrow a book",
            description = "Creates a borrowing record and marks the selected book copy as borrowed."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Book borrowed successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Borrower or book copy not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Book copy is already borrowed"
            )
    })
    public ResponseEntity<BorrowResponse> borrowBook(
            @Valid @RequestBody BorrowRequest request) {

        return ResponseEntity.ok(
                borrowService.borrowBook(request));
    }

    @PostMapping("/return")
    @Operation(
            summary = "Return a borrowed book",
            description = "Returns a borrowed book copy and marks it as available."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Book returned successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Borrow record or book copy not found"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Book has already been returned"
            )
    })
    public ResponseEntity<Void> returnBook(
            @Valid @RequestBody ReturnRequest returnRequest) {

        borrowService.returnBook(returnRequest);

        return ResponseEntity.ok().build();
    }

}
