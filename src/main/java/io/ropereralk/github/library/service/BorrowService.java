package io.ropereralk.github.library.service;

import io.ropereralk.github.library.dto.request.BorrowRequest;
import io.ropereralk.github.library.dto.request.ReturnRequest;
import io.ropereralk.github.library.dto.response.BorrowResponse;
import io.ropereralk.github.library.enumeration.BookCopyStatus;
import io.ropereralk.github.library.enumeration.BorrowStatus;
import io.ropereralk.github.library.exception.*;
import io.ropereralk.github.library.model.BookCopy;
import io.ropereralk.github.library.model.BorrowRecord;
import io.ropereralk.github.library.model.Borrower;
import io.ropereralk.github.library.repository.BookCopyRepository;
import io.ropereralk.github.library.repository.BorrowRecordRepository;
import io.ropereralk.github.library.repository.BorrowerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
@RequiredArgsConstructor
public class BorrowService {

    private final BorrowRecordRepository borrowRecordRepository;
    private final BorrowerRepository borrowerRepository;
    private final BookCopyRepository bookCopyRepository;

    /**
     * Borrows an available book copy for a borrower.
     *
     * @param borrowRequest borrower and book copy details
     * @return borrow transaction details
     * @throws BorrowerNotFoundException    if the borrower does not exist
     * @throws BookCopyNotFoundException    if the book copy does not exist
     * @throws BookAlreadyBorrowedException if the book copy is unavailable
     */
    @Transactional
    public BorrowResponse borrowBook(final BorrowRequest borrowRequest) {

        Borrower borrower = borrowerRepository.findById(
                        borrowRequest.borrowerId())
                .orElseThrow(() ->
                        new BorrowerNotFoundException(
                                "Borrower not found"));

        // Lock the book copy record to prevent concurrent borrow requests
        BookCopy bookCopy = bookCopyRepository
                .findByIdForUpdate(borrowRequest.bookCopyId())
                .orElseThrow(() ->
                        new BookCopyNotFoundException(
                                "Book copy not found"));

        if (bookCopy.getStatus() != BookCopyStatus.AVAILABLE) {
            throw new BookAlreadyBorrowedException(
                    "Book copy is already borrowed");
        }

        bookCopy.setStatus(BookCopyStatus.BORROWED);

        BorrowRecord borrowRecord =
                borrowRecordRepository.save(
                        BorrowRecord.builder()
                                .borrower(borrower)
                                .bookCopy(bookCopy)
                                .status(BorrowStatus.ACTIVE)
                                .build()
                );

        return BorrowResponse.builder()
                .borrowerId(borrower.getId())
                .borrowerName(borrower.getName())
                .bookCopyId(bookCopy.getId())
                .isbn(bookCopy.getBook().getIsbn())
                .bookCopyNumber(bookCopy.getCopyNumber())
                .borrowedDate(borrowRecord.getBorrowedDate())
                .build();
    }

    /**
     * Returns a borrowed book copy and marks the borrow record as returned.
     *
     * @param request book return details
     * @throws BorrowRecordNotFoundException if no active borrow record exists
     * @throws BookAlreadyReturnedException  if the book copy is already available
     */
    @Transactional
    public void returnBook(final ReturnRequest request) {

        BorrowRecord borrowRecord = borrowRecordRepository
                .findByBookCopyIdAndStatus(
                        request.bookCopyId(),
                        BorrowStatus.ACTIVE)
                .orElseThrow(() ->
                        new BorrowRecordNotFoundException(
                                "No active borrow record found for book copy id: "
                                        + request.bookCopyId()));

        BookCopy bookCopy = borrowRecord.getBookCopy();

        if (bookCopy.getStatus() != BookCopyStatus.BORROWED) {
            throw new BookAlreadyReturnedException(
                    "Book copy has already been returned");
        }

        borrowRecord.setStatus(BorrowStatus.RETURNED);
        borrowRecord.setReturnedDate(Instant.now());

        bookCopy.setStatus(BookCopyStatus.AVAILABLE);
    }
}
