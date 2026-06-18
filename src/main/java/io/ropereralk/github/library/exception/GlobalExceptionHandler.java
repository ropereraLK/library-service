package io.ropereralk.github.library.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(
            MethodArgumentNotValidException ex) {

        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        problem.setTitle("Validation Failed");

        Map<String, String> errors = new HashMap<>();

        ex.getFieldErrors().forEach(error ->
                errors.put(
                        error.getField(),
                        error.getDefaultMessage()));

        problem.setProperty("errors", errors);

        return problem;
    }

    @ExceptionHandler(DuplicateBorrowerException.class)
    public ProblemDetail handleDuplicateBorrower(
            DuplicateBorrowerException ex) {

        ProblemDetail problem =
                ProblemDetail.forStatus(HttpStatus.CONFLICT);

        problem.setTitle("Duplicate Borrower");
        problem.setDetail(ex.getMessage());

        return problem;
    }

}