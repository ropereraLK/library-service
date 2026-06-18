package io.ropereralk.github.library.repository;

import io.ropereralk.github.library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    boolean existsByIsbn(String isbn);
}
