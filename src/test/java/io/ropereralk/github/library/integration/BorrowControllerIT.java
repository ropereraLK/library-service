package io.ropereralk.github.library.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ropereralk.github.library.dto.request.BorrowRequest;
import io.ropereralk.github.library.dto.request.ReturnRequest;
import io.ropereralk.github.library.enumeration.BookCopyStatus;
import io.ropereralk.github.library.enumeration.BorrowStatus;
import io.ropereralk.github.library.model.Book;
import io.ropereralk.github.library.model.BookCopy;
import io.ropereralk.github.library.model.BorrowRecord;
import io.ropereralk.github.library.model.Borrower;
import io.ropereralk.github.library.repository.BookCopyRepository;
import io.ropereralk.github.library.repository.BookRepository;
import io.ropereralk.github.library.repository.BorrowRecordRepository;
import io.ropereralk.github.library.repository.BorrowerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BorrowControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private BookCopyRepository bookCopyRepository;

    @Autowired
    private BorrowRecordRepository borrowRecordRepository;

    @Test
    void borrowBook_shouldBorrowSuccessfully() throws Exception {
        Borrower borrower = borrowerRepository.save(
                Borrower.builder().name("John Doe").email("john@test.com").build());

        Book book = bookRepository.save(
                Book.builder().title("Clean Code").author("Robert Martin")
                        .isbn("9780132350884").noOfCopies(1).build());

        BookCopy bookCopy = bookCopyRepository.save(
                BookCopy.builder().book(book).copyNumber(1)
                        .status(BookCopyStatus.AVAILABLE).build());

        BorrowRequest request =
                new BorrowRequest(borrower.getId(), bookCopy.getId());

        mockMvc.perform(post("/v1/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.borrowerId").value(borrower.getId()));

        BookCopy updated =
                bookCopyRepository.findById(bookCopy.getId()).orElseThrow();

        assertEquals(BookCopyStatus.BORROWED, updated.getStatus());
    }

    @Test
    void borrowBook_shouldReturn404WhenBorrowerNotFound() throws Exception {

        Book book = bookRepository.save(
                Book.builder().title("Clean Code").author("Author")
                        .isbn("ISBN-1").noOfCopies(1).build());

        BookCopy copy = bookCopyRepository.save(
                BookCopy.builder().book(book).copyNumber(1)
                        .status(BookCopyStatus.AVAILABLE).build());

        BorrowRequest request = new BorrowRequest(999L, copy.getId());

        mockMvc.perform(post("/v1/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void borrowBook_shouldReturn404WhenBookCopyNotFound() throws Exception {

        Borrower borrower = borrowerRepository.save(
                Borrower.builder().name("John Doe").email("john2@test.com").build());

        BorrowRequest request = new BorrowRequest(borrower.getId(), 999L);

        mockMvc.perform(post("/v1/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void borrowBook_shouldReturn409WhenBookAlreadyBorrowed() throws Exception {

        Borrower borrower = borrowerRepository.save(
                Borrower.builder().name("John Doe").email("john3@test.com").build());

        Book book = bookRepository.save(
                Book.builder().title("Clean Code").author("Author")
                        .isbn("ISBN-2").noOfCopies(1).build());

        BookCopy copy = bookCopyRepository.save(
                BookCopy.builder().book(book).copyNumber(1)
                        .status(BookCopyStatus.BORROWED).build());

        BorrowRequest request = new BorrowRequest(borrower.getId(), copy.getId());

        mockMvc.perform(post("/v1/borrow")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    @Test
    void returnBook_shouldReturnSuccessfully() throws Exception {

        Book book = bookRepository.save(
                Book.builder().title("Clean Code").author("Author")
                        .isbn("ISBN-3").noOfCopies(1).build());

        BookCopy copy = bookCopyRepository.save(
                BookCopy.builder().book(book).copyNumber(1)
                        .status(BookCopyStatus.BORROWED).build());

        Borrower borrower = borrowerRepository.save(
                Borrower.builder().name("John Doe").email("john4@test.com").build());

        BorrowRecord record = borrowRecordRepository.save(
                BorrowRecord.builder()
                        .borrower(borrower)
                        .bookCopy(copy)
                        .status(BorrowStatus.ACTIVE)
                        .borrowedDate(Instant.now())
                        .build());

        ReturnRequest request = new ReturnRequest(copy.getId());

        mockMvc.perform(post("/v1/borrow/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        assertEquals(BorrowStatus.RETURNED, record.getStatus());
        assertEquals(BookCopyStatus.AVAILABLE, copy.getStatus());
        assertNotNull(record.getReturnedDate());
    }

    @Test
    void returnBook_shouldReturn404WhenBorrowRecordNotFound() throws Exception {

        ReturnRequest request = new ReturnRequest(999L);

        mockMvc.perform(post("/v1/borrow/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    void returnBook_shouldReturn409WhenBookAlreadyReturned() throws Exception {

        Book book = bookRepository.save(
                Book.builder().title("Clean Code").author("Author")
                        .isbn("ISBN-4").noOfCopies(1).build());

        BookCopy copy = bookCopyRepository.save(
                BookCopy.builder().book(book).copyNumber(1)
                        .status(BookCopyStatus.AVAILABLE).build());

        Borrower borrower = borrowerRepository.save(
                Borrower.builder().name("John Doe").email("john5@test.com").build());

        borrowRecordRepository.save(
                BorrowRecord.builder()
                        .borrower(borrower)
                        .bookCopy(copy)
                        .status(BorrowStatus.ACTIVE)
                        .build());

        ReturnRequest request = new ReturnRequest(copy.getId());

        mockMvc.perform(post("/v1/borrow/return")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }
}