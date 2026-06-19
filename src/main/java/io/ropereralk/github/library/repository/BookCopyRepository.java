package io.ropereralk.github.library.repository;

import io.ropereralk.github.library.model.BookCopy;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

    /**
     * Retrieves a book copy using a pessimistic write lock
     * to prevent concurrent borrow operations.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select bc
            from BookCopy bc
            where bc.id = :id
            """)
    Optional<BookCopy> findByIdForUpdate(Long id);

}
