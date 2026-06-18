package io.ropereralk.github.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Entity
@Table(name = "books")
@Data
@Builder
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, unique = true)
    private String isbn;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String title;

    /**
     * Kept as a String for simplicity and to align with the assignment requirements.
     * In a production system, Author would likely be modeled as a separate entity.
     */
    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String author;

    @Min(1)
    @Column(nullable = false)
    private Integer noOfCopies;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;
}
