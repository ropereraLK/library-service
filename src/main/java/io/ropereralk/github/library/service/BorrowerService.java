package io.ropereralk.github.library.service;

import io.ropereralk.github.library.dto.request.BorrowerRequest;
import io.ropereralk.github.library.dto.response.BorrowerResponse;
import io.ropereralk.github.library.exception.DuplicateBorrowerException;
import io.ropereralk.github.library.model.Borrower;
import io.ropereralk.github.library.repository.BorrowerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    /**
     * Creates a new borrower if the email address is not already registered.
     *
     * @param borrowerRequest borrower details
     * @return created borrower details
     * @throws DuplicateBorrowerException if a borrower with the same email already exists
     */
    @Transactional
    public BorrowerResponse createBorrower(final BorrowerRequest borrowerRequest) {

        if (borrowerRepository.existsByEmail(
                borrowerRequest.email())) {

            throw new DuplicateBorrowerException(
                    "Borrower already exists with email: "
                            + borrowerRequest.email());
        }

        Borrower savedBorrower =
                borrowerRepository.save(Borrower.builder()
                        .name(borrowerRequest.name())
                        .email(borrowerRequest.email())
                        .build());

        return new BorrowerResponse(
                savedBorrower.getId(),
                savedBorrower.getName(),
                savedBorrower.getEmail());
    }
}
