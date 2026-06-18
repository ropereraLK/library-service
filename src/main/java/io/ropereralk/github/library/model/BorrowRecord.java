package io.ropereralk.github.library.model;

import io.ropereralk.github.library.enumuration.BorrowStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@Builder
@Table(
        name = "borrow_records",
        indexes = {
                @Index(
                        name = "idx_borrow_record_copy_status",
                        columnList = "book_copy_id,status"
                )
        }
)
public class BorrowRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_copy_id", nullable = false)
    private BookCopy bookCopy;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Borrower borrower;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant borrowedDate;

    //TODO: Logic to be implemented
    private LocalDate returnDueDate;

    //TODO: Logic to be implemented
    private Instant returnedDate;

    private BorrowStatus status;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;


}
