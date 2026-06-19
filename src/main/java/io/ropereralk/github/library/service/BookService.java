package io.ropereralk.github.library.service;

import io.ropereralk.github.library.dto.request.BookRequest;
import io.ropereralk.github.library.dto.response.BookResponse;
import io.ropereralk.github.library.enumeration.BookCopyStatus;
import io.ropereralk.github.library.exception.DuplicateBookException;
import io.ropereralk.github.library.model.Book;
import io.ropereralk.github.library.model.BookCopy;
import io.ropereralk.github.library.repository.BookRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final BookCopyService bookCopyService;

    /**
     * Creates a new book and its associated copies.
     *
     * @param bookRequest book details and number of copies to create
     * @return created book details
     * @throws DuplicateBookException if a book with the same ISBN already exists
     */
    @Transactional
    public BookResponse createBook(final BookRequest bookRequest) {

        if (bookRepository.existsByIsbn(bookRequest.isbn())) {
            throw new DuplicateBookException(
                    "Book already exists with ISBN: " + bookRequest.isbn());
        }

        Book savedBook = bookRepository.save(
                Book.builder()
                        .title(bookRequest.title())
                        .author(bookRequest.author())
                        .isbn(bookRequest.isbn())
                        .build()
        );

        bookCopyService.createBookCopies(
                createCopies(savedBook, bookRequest.noOfCopies()));

        return BookResponse.builder()
                .title(savedBook.getTitle())
                .author(savedBook.getAuthor())
                .isbn(savedBook.getIsbn())
                .build();
    }

    /**
     * Retrieves a paginated list of books.
     *
     * @param pageable pagination information
     * @return paginated book responses
     */
    public Page<BookResponse> getAllBooks(final Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(book -> new BookResponse(
                        book.getTitle(),
                        book.getAuthor(),
                        book.getIsbn()
                ));
    }

    /**
     * Creates the requested number of available copies for a book.
     */
    private List<BookCopy> createCopies(
            Book book,
            int noOfCopies) {

        // Generate sequential copy numbers starting from 1
        return IntStream.range(0, noOfCopies)
                .mapToObj(i -> BookCopy.builder()
                        .book(book)
                        .copyNumber(i + 1)
                        .status(BookCopyStatus.AVAILABLE)
                        .build())
                .toList();
    }
}
