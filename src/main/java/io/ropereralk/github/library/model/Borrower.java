package io.ropereralk.github.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Builder
@Entity
@Table(name = "borrowers")
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 200)
    @Column(nullable = false)
    private String name;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(nullable = false, unique = true)
    private String email;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

}
