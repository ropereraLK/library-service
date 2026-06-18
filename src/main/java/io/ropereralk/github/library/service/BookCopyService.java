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

    @Transactional
    public List<BookCopy> createBookCopies(List<BookCopy> bookCopies){
        if (bookCopies == null || bookCopies.isEmpty()) {
            throw new IllegalArgumentException(
                    "Book copies cannot be 0");
        }

        return bookCopyRepository.saveAll(bookCopies);
    }








}
