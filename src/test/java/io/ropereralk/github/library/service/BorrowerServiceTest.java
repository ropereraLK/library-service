package io.ropereralk.github.library.service;

import io.ropereralk.github.library.dto.request.BorrowerRequest;
import io.ropereralk.github.library.dto.response.BorrowerResponse;
import io.ropereralk.github.library.exception.DuplicateBorrowerException;
import io.ropereralk.github.library.model.Borrower;
import io.ropereralk.github.library.repository.BorrowerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowerService borrowerService;

    @Test
    void createBorrower_shouldCreateBorrowerSuccessfully() {

        // Given
        BorrowerRequest request = new BorrowerRequest(
                "John Doe",
                "john.doe@example.com"
        );

        Borrower savedBorrower = Borrower.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();

        when(borrowerRepository.existsByEmail(request.email()))
                .thenReturn(false);

        when(borrowerRepository.save(any(Borrower.class)))
                .thenReturn(savedBorrower);

        // When
        BorrowerResponse response =
                borrowerService.createBorrower(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("John Doe", response.name());
        assertEquals("john.doe@example.com", response.email());

        verify(borrowerRepository)
                .save(any(Borrower.class));
    }

    @Test
    void createBorrower_shouldThrowDuplicateBorrowerException_whenEmailAlreadyExists() {

        // Given
        BorrowerRequest request = new BorrowerRequest(
                "John Doe",
                "john.doe@example.com"
        );

        when(borrowerRepository.existsByEmail(request.email()))
                .thenReturn(true);

        // When + Then
        assertThrows(
                DuplicateBorrowerException.class,
                () -> borrowerService.createBorrower(request)
        );

        verify(borrowerRepository, never())
                .save(any(Borrower.class));
    }

    @Test
    void createBorrower_shouldCheckForDuplicateEmailBeforeSaving() {

        // Given
        BorrowerRequest request = new BorrowerRequest(
                "John Doe",
                "john.doe@example.com"
        );

        when(borrowerRepository.existsByEmail(request.email()))
                .thenReturn(true);

        // When + Then
        assertThrows(
                DuplicateBorrowerException.class,
                () -> borrowerService.createBorrower(request)
        );

        verify(borrowerRepository)
                .existsByEmail(request.email());
    }

}