package io.ropereralk.github.library.contoller;

import io.ropereralk.github.library.dto.request.BookRequest;
import io.ropereralk.github.library.dto.response.BookResponse;
import io.ropereralk.github.library.service.BookService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/book")
@Tag(
        name = "Books",
        description = "Operations related to library books"
)
public class BookController {

    private final BookService bookService;

    @PostMapping
    @Operation(
            summary = "Register a new book",
            description = "Creates a new book and automatically creates the specified number of copies."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Book created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A book with the same ISBN already exists"
            )
    })
    public ResponseEntity<BookResponse> createBook(
            @Valid @RequestBody BookRequest bookRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(bookService.createBook(bookRequest));
    }

    @GetMapping
    @Operation(
            summary = "Get all books",
            description = "Retrieves a paginated list of all books in the library."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Books retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination parameters"
            )
    })
    public ResponseEntity<Page<BookResponse>> getAllBooks(
            @PageableDefault(page = 0, size = 20)
            Pageable pageable) {

        if (pageable.getPageSize() > 100) {
            throw new IllegalArgumentException(
                    "Page size cannot exceed 100");
        }

        return ResponseEntity.ok(
                bookService.getAllBooks(pageable));
    }

}