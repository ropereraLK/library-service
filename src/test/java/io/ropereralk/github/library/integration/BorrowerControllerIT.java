package io.ropereralk.github.library.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ropereralk.github.library.dto.request.BorrowerRequest;
import io.ropereralk.github.library.model.Borrower;
import io.ropereralk.github.library.repository.BorrowerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BorrowerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BorrowerRepository borrowerRepository;

    @Test
    void createBorrower_shouldReturn201Created() throws Exception {

        BorrowerRequest request =
                new BorrowerRequest(
                        "John Doe",
                        "john.doe@example.com"
                );

        mockMvc.perform(
                        post("/v1/borrowers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name")
                        .value("John Doe"))
                .andExpect(jsonPath("$.email")
                        .value("john.doe@example.com"));
    }

    @Test
    void createBorrower_shouldReturn409WhenEmailAlreadyExists()
            throws Exception {

        borrowerRepository.save(
                Borrower.builder()
                        .name("John Doe")
                        .email("john.doe@example.com")
                        .build()
        );

        BorrowerRequest request =
                new BorrowerRequest(
                        "John Doe",
                        "john.doe@example.com"
                );

        mockMvc.perform(
                        post("/v1/borrowers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isConflict());
    }

    @Test
    void createBorrower_shouldReturn400WhenEmailIsInvalid()
            throws Exception {

        String payload = """
                {
                  "name":"John Doe",
                  "email":"invalid-email"
                }
                """;

        mockMvc.perform(
                        post("/v1/borrowers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    void createBorrower_shouldReturn400WhenNameIsBlank()
            throws Exception {

        String payload = """
                {
                  "name":"",
                  "email":"rohan@example.com"
                }
                """;

        mockMvc.perform(
                        post("/v1/borrowers")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isBadRequest());
    }
}