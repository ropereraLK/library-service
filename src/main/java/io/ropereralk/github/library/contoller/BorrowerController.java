package io.ropereralk.github.library.contoller;

import io.ropereralk.github.library.dto.request.BorrowerRequest;
import io.ropereralk.github.library.dto.response.BorrowerResponse;
import io.ropereralk.github.library.service.BorrowerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/borrowers")
@Tag(
        name = "Borrowers",
        description = "Operations related to library members and borrowers"
)
public class BorrowerController {

    private final BorrowerService borrowerService;

    @PostMapping
    @Operation(
            summary = "Create a borrower",
            description = "Registers a new borrower in the library system."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Borrower created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation failed"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "A borrower with the same email already exists"
            )
    })
    public ResponseEntity<BorrowerResponse> createBorrower(
            @Valid @RequestBody BorrowerRequest borrowerRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(borrowerService.createBorrower(borrowerRequest));
    }

}