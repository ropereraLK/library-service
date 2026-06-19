package io.ropereralk.github.library.service;

import static org.junit.jupiter.api.Assertions.*;

import io.ropereralk.github.library.dto.request.BorrowRequest;
import io.ropereralk.github.library.dto.request.ReturnRequest;
import io.ropereralk.github.library.dto.response.BorrowResponse;
import io.ropereralk.github.library.enumeration.BookCopyStatus;
import io.ropereralk.github.library.enumeration.BorrowStatus;
import io.ropereralk.github.library.exception.*;
import io.ropereralk.github.library.model.Book;
import io.ropereralk.github.library.model.BookCopy;
import io.ropereralk.github.library.model.BorrowRecord;
import io.ropereralk.github.library.model.Borrower;
import io.ropereralk.github.library.repository.BookCopyRepository;
import io.ropereralk.github.library.repository.BorrowRecordRepository;
import io.ropereralk.github.library.repository.BorrowerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class BorrowServiceTest {

    @Mock
    private BorrowRecordRepository borrowRecordRepository;

    @Mock
    private BorrowerRepository borrowerRepository;

    @Mock
    private BookCopyRepository bookCopyRepository;

    @InjectMocks
    private BorrowService borrowService;

    @Test
    void borrowBook_shouldBorrowBookSuccessfully() {

        // Given
        BorrowRequest request =
                new BorrowRequest(1L, 100L);

        Borrower borrower = Borrower.builder()
                .id(1L)
                .name("John Doe")
                .email("john@test.com")
                .build();

        Book book = Book.builder()
                .isbn("9780132350884")
                .title("Clean Code")
                .build();

        BookCopy bookCopy = BookCopy.builder()
                .id(100L)
                .book(book)
                .copyNumber(1)
                .status(BookCopyStatus.AVAILABLE)
                .build();

        BorrowRecord borrowRecord =
                BorrowRecord.builder()
                        .borrower(borrower)
                        .bookCopy(bookCopy)
                        .status(BorrowStatus.ACTIVE)
                        .borrowedDate(Instant.now())
                        .build();

        when(borrowerRepository.findById(1L))
                .thenReturn(Optional.of(borrower));

        when(bookCopyRepository.findByIdForUpdate(100L))
                .thenReturn(Optional.of(bookCopy));

        when(borrowRecordRepository.save(any(BorrowRecord.class)))
                .thenReturn(borrowRecord);

        // When
        BorrowResponse response =
                borrowService.borrowBook(request);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.borrowerId());
        assertEquals("John Doe", response.borrowerName());
        assertEquals(100L, response.bookCopyId());

        verify(borrowRecordRepository)
                .save(any(BorrowRecord.class));
    }

    @Test
    void borrowBook_shouldThrowBorrowerNotFoundException() {

        // Given
        BorrowRequest request =
                new BorrowRequest(1L, 100L);

        when(borrowerRepository.findById(1L))
                .thenReturn(Optional.empty());

        // When + Then
        assertThrows(
                BorrowerNotFoundException.class,
                () -> borrowService.borrowBook(request)
        );
    }

    @Test
    void borrowBook_shouldThrowBookCopyNotFoundException() {

        // Given
        BorrowRequest request =
                new BorrowRequest(1L, 100L);

        Borrower borrower = Borrower.builder()
                .id(1L)
                .build();

        when(borrowerRepository.findById(1L))
                .thenReturn(Optional.of(borrower));

        when(bookCopyRepository.findByIdForUpdate(100L))
                .thenReturn(Optional.empty());

        // When + Then
        assertThrows(
                BookCopyNotFoundException.class,
                () -> borrowService.borrowBook(request)
        );
    }

    @Test
    void borrowBook_shouldThrowBookAlreadyBorrowedException() {

        // Given
        BorrowRequest request =
                new BorrowRequest(1L, 100L);

        Borrower borrower = Borrower.builder()
                .id(1L)
                .build();

        BookCopy bookCopy = BookCopy.builder()
                .id(100L)
                .status(BookCopyStatus.BORROWED)
                .build();

        when(borrowerRepository.findById(1L))
                .thenReturn(Optional.of(borrower));

        when(bookCopyRepository.findByIdForUpdate(100L))
                .thenReturn(Optional.of(bookCopy));

        // When + Then
        assertThrows(
                BookAlreadyBorrowedException.class,
                () -> borrowService.borrowBook(request)
        );
    }

    @Test
    void returnBook_shouldReturnBookSuccessfully() {

        // Given
        ReturnRequest request =
                new ReturnRequest(100L);

        BookCopy bookCopy = BookCopy.builder()
                .id(100L)
                .status(BookCopyStatus.BORROWED)
                .build();

        BorrowRecord borrowRecord =
                BorrowRecord.builder()
                        .bookCopy(bookCopy)
                        .status(BorrowStatus.ACTIVE)
                        .build();

        when(borrowRecordRepository
                .findByBookCopyIdAndStatus(
                        100L,
                        BorrowStatus.ACTIVE))
                .thenReturn(Optional.of(borrowRecord));

        // When
        borrowService.returnBook(request);

        // Then
        assertEquals(
                BorrowStatus.RETURNED,
                borrowRecord.getStatus());

        assertEquals(
                BookCopyStatus.AVAILABLE,
                bookCopy.getStatus());

        assertNotNull(
                borrowRecord.getReturnedDate());
    }

    @Test
    void returnBook_shouldThrowBorrowRecordNotFoundException() {

        // Given
        ReturnRequest request =
                new ReturnRequest(100L);

        when(borrowRecordRepository
                .findByBookCopyIdAndStatus(
                        100L,
                        BorrowStatus.ACTIVE))
                .thenReturn(Optional.empty());

        // When + Then
        assertThrows(
                BorrowRecordNotFoundException.class,
                () -> borrowService.returnBook(request)
        );
    }

    @Test
    void returnBook_shouldThrowBookAlreadyReturnedException() {

        // Given
        ReturnRequest request =
                new ReturnRequest(100L);

        BookCopy bookCopy = BookCopy.builder()
                .status(BookCopyStatus.AVAILABLE)
                .build();

        BorrowRecord borrowRecord =
                BorrowRecord.builder()
                        .bookCopy(bookCopy)
                        .status(BorrowStatus.ACTIVE)
                        .build();

        when(borrowRecordRepository
                .findByBookCopyIdAndStatus(
                        100L,
                        BorrowStatus.ACTIVE))
                .thenReturn(Optional.of(borrowRecord));

        // When + Then
        assertThrows(
                BookAlreadyReturnedException.class,
                () -> borrowService.returnBook(request)
        );
    }

}