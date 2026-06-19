package io.ropereralk.github.library.service;

import io.ropereralk.github.library.model.BookCopy;
import io.ropereralk.github.library.repository.BookCopyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCopyService {
    private final BookCopyRepository bookCopyRepository;

    /**
     * Creates and persists a collection of book copies.
     *
     * @param bookCopies list of book copies to be saved
     * @return persisted book copies
     * @throws IllegalArgumentException if the list is null or empty
     */
    @Transactional
    public List<BookCopy> createBookCopies(List<BookCopy> bookCopies){

        // Ensure at least one book copy is provided
        if (bookCopies == null || bookCopies.isEmpty()) {
            throw new IllegalArgumentException(
                    "Book copies cannot be 0");
        }

        // Persist all book copies in a single transaction
        return bookCopyRepository.saveAll(bookCopies);
    }








}
