package io.ropereralk.github.library.service;

import static org.junit.jupiter.api.Assertions.*;

import io.ropereralk.github.library.dto.request.BookRequest;
import io.ropereralk.github.library.dto.response.BookResponse;
import io.ropereralk.github.library.exception.DuplicateBookException;
import io.ropereralk.github.library.model.Book;
import io.ropereralk.github.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookCopyService bookCopyService;

    @InjectMocks
    private BookService bookService;

    @Test
    void createBook_shouldCreateBookSuccessfully() {

        // Given
        BookRequest request = new BookRequest(
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                2
        );

        Book savedBook = Book.builder()
                .id(1L)
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .build();

        when(bookRepository.existsByIsbn(request.isbn()))
                .thenReturn(false);

        when(bookRepository.save(any(Book.class)))
                .thenReturn(savedBook);

        // When
        BookResponse response =
                bookService.createBook(request);

        // Then
        assertNotNull(response);
        assertEquals("Clean Code", response.title());
        assertEquals("Robert C. Martin", response.author());
        assertEquals("9780132350884", response.isbn());

        verify(bookRepository).save(any(Book.class));
        verify(bookCopyService).createBookCopies(anyList());
    }

    @Test
    void createBook_shouldThrowDuplicateBookException_whenBookAlreadyExists() {

        // Given
        BookRequest request = new BookRequest(
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                2
        );

        when(bookRepository.existsByIsbn(request.isbn()))
                .thenReturn(true);

        // When + Then
        assertThrows(
                DuplicateBookException.class,
                () -> bookService.createBook(request)
        );

        verify(bookRepository, never())
                .save(any(Book.class));

        verify(bookCopyService, never())
                .createBookCopies(anyList());
    }

    @Test
    void createBook_shouldCreateRequestedNumberOfCopies() {

        // Given
        BookRequest request = new BookRequest(
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                5
        );

        Book savedBook = Book.builder()
                .id(1L)
                .title(request.title())
                .author(request.author())
                .isbn(request.isbn())
                .build();

        when(bookRepository.existsByIsbn(request.isbn()))
                .thenReturn(false);

        when(bookRepository.save(any(Book.class)))
                .thenReturn(savedBook);

        // When
        bookService.createBook(request);

        // Then
        verify(bookCopyService)
                .createBookCopies(
                        argThat(copies -> copies.size() == 5)
                );
    }

    @Test
    void createBook_shouldCheckForDuplicateIsbnBeforeSaving() {

        // Given
        BookRequest request = new BookRequest(
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                2
        );

        when(bookRepository.existsByIsbn(request.isbn()))
                .thenReturn(true);

        // When + Then
        assertThrows(
                DuplicateBookException.class,
                () -> bookService.createBook(request)
        );

        verify(bookRepository)
                .existsByIsbn(request.isbn());
    }

    @Test
    void getAllBooks_shouldReturnPaginatedBooks() {

        // Given
        Book book = Book.builder()
                .title("Clean Code")
                .author("Robert C. Martin")
                .isbn("9780132350884")
                .build();

        Pageable pageable = PageRequest.of(0, 20);

        Page<Book> books =
                new PageImpl<>(List.of(book));

        when(bookRepository.findAll(pageable))
                .thenReturn(books);

        // When
        Page<BookResponse> response =
                bookService.getAllBooks(pageable);

        // Then
        assertEquals(1, response.getTotalElements());

        assertFalse(response.getContent().isEmpty());

        BookResponse firstBook =
                response.getContent().get(0);

        assertEquals("Clean Code", firstBook.title());
        assertEquals("Robert C. Martin", firstBook.author());
        assertEquals("9780132350884", firstBook.isbn());

        verify(bookRepository)
                .findAll(pageable);
    }

}
