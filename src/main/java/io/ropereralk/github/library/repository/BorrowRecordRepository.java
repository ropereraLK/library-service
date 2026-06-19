package io.ropereralk.github.library.repository;

import io.ropereralk.github.library.enumeration.BorrowStatus;
import io.ropereralk.github.library.model.BorrowRecord;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select br
            from BorrowRecord br
            where br.id = :id
            """)
    Optional<BorrowRecord> findByIdForUpdate(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<BorrowRecord> findByBookCopyIdAndStatus(
            Long bookCopyId,
            BorrowStatus status);
}
