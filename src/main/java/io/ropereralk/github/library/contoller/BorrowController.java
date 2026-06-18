package io.ropereralk.github.library.contoller;


import io.ropereralk.github.library.dto.request.BorrowRequest;
import io.ropereralk.github.library.dto.request.ReturnRequest;
import io.ropereralk.github.library.dto.response.BorrowResponse;
import io.ropereralk.github.library.service.BorrowService;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;


@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/borrow")
@Tag(name = "Borrow APIs")
public class BorrowController {

    private final BorrowService borrowService;

    @PostMapping
    @Operation(summary = "Borrow a book")
    public ResponseEntity<BorrowResponse> borrowBook(
            @Valid @RequestBody BorrowRequest request) {

        return ResponseEntity.ok(
                borrowService.borrowBook(request));
    }

    @PostMapping("/return")
    @Operation(summary = "Return a borrowed book")
    public ResponseEntity<Void> returnBook(
            @Valid @RequestBody ReturnRequest returnRequest) {

        borrowService.returnBook(returnRequest);

        return ResponseEntity.ok().build();
    }


}
