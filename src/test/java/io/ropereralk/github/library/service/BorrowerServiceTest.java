package io.ropereralk.github.library.service;

import io.ropereralk.github.library.dto.request.BorrowerRequest;
import io.ropereralk.github.library.exception.DuplicateBorrowerException;
import io.ropereralk.github.library.repository.BorrowerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BorrowerServiceTest {

    @Mock
    private BorrowerRepository borrowerRepository;

    @InjectMocks
    private BorrowerService borrowerService;

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {

        BorrowerRequest request =
                new BorrowerRequest(
                        "Rohan Perera",
                        "rohan@example.com");

        when(borrowerRepository.existsByEmail(
                "rohan@example.com"))
                .thenReturn(true);

        assertThrows(
                DuplicateBorrowerException.class,
                () -> borrowerService.createBorrower(request));

        verify(borrowerRepository, never())
                .save(any());
    }
}