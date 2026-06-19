package io.ropereralk.github.library.service;

import static org.junit.jupiter.api.Assertions.*;

import io.ropereralk.github.library.model.BookCopy;
import io.ropereralk.github.library.repository.BookCopyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookCopyServiceTest {

    @Mock
    private BookCopyRepository bookCopyRepository;

    @InjectMocks
    private BookCopyService bookCopyService;

    @Test
    void createBookCopies_shouldSaveCopiesSuccessfully() {

        // Given
        List<BookCopy> copies = List.of(
                BookCopy.builder()
                        .copyNumber(1)
                        .build()
        );

        when(bookCopyRepository.saveAll(copies))
                .thenReturn(copies);
        // When
        List<BookCopy> result =
                bookCopyService.createBookCopies(copies);

        // Then
        assertEquals(1, result.size());

        verify(bookCopyRepository)
                .saveAll(copies);
    }

    @Test
    void createBookCopies_shouldThrowExceptionWhenCopiesAreNull() {

        // When + Then
        assertThrows(
                IllegalArgumentException.class,
                () -> bookCopyService.createBookCopies(null)
        );

        verify(bookCopyRepository, never())
                .saveAll(anyList());
    }

    @Test
    void createBookCopies_shouldThrowExceptionWhenCopiesAreEmpty() {

        // Given
        List<BookCopy> copies = Collections.emptyList();

        // When + Then
        assertThrows(
                IllegalArgumentException.class,
                () -> bookCopyService.createBookCopies(copies)
        );

        verify(bookCopyRepository, never())
                .saveAll(anyList());
    }
}