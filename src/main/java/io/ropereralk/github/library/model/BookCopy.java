package io.ropereralk.github.library.model;

import io.ropereralk.github.library.enumuration.BookCopyStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.Lazy;

import java.time.Instant;

@Entity
@Table(name = "BookCopies",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {"book_id", "copy_number"}
                )
        })
@Data
@Builder
public class BookCopy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lazy
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @Min(1)
    @Column(nullable = false)
    private Integer copyNumber;

    @NotBlank
    @Column(nullable = false)
    @Enumerated
    private BookCopyStatus status;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
