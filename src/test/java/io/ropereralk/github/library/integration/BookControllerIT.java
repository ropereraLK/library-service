package io.ropereralk.github.library.integration;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.ropereralk.github.library.dto.request.BookRequest;
import io.ropereralk.github.library.model.Book;
import io.ropereralk.github.library.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class BookControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createBook_shouldReturn201Created() throws Exception {

        // Given
        BookRequest request = new BookRequest(
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                3
        );

        // When + Then
        mockMvc.perform(
                        post("/v1/book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title")
                        .value("Clean Code"))
                .andExpect(jsonPath("$.author")
                        .value("Robert C. Martin"))
                .andExpect(jsonPath("$.isbn")
                        .value("9780132350884"));

        assertTrue(
                bookRepository.existsByIsbn(
                        "9780132350884"
                )
        );
    }

    @Test
    void getAllBooks_shouldReturnBooks() throws Exception {

        // Given
        bookRepository.save(
                Book.builder()
                        .title("Clean Code")
                        .author("Robert C. Martin")
                        .isbn("9780132350884")
                        .build()
        );

        // When + Then
        mockMvc.perform(
                        get("/v1/book")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].title")
                        .value("Clean Code"))
                .andExpect(jsonPath("$.content[0].author")
                        .value("Robert C. Martin"))
                .andExpect(jsonPath("$.content[0].isbn")
                        .value("9780132350884"));
    }

    //Test for duplicate book
    @Test
    void createBook_shouldReturn409WhenBookAlreadyExists()
            throws Exception {

        // Given
        bookRepository.save(
                Book.builder()
                        .title("Clean Code")
                        .author("Robert C. Martin")
                        .isbn("9780132350884")
                        .build()
        );

        BookRequest request = new BookRequest(
                "Clean Code",
                "Robert C. Martin",
                "9780132350884",
                2
        );

        // When + Then
        mockMvc.perform(
                        post("/v1/book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(
                                        objectMapper.writeValueAsString(request)
                                )
                )
                .andExpect(status().isConflict());
    }

    //Test for validation error
    @Test
    void createBook_shouldReturn400WhenTitleIsBlank()
            throws Exception {

        // Given
        String payload = """
            {
              "title":"",
              "author":"Robert C. Martin",
              "isbn":"9780132350884",
              "noOfCopies":2
            }
            """;

        // When + Then
        mockMvc.perform(
                        post("/v1/book")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(payload)
                )
                .andExpect(status().isBadRequest());
    }

    //Test for Pagination validation
    @Test
    void getAllBooks_shouldReturn400WhenPageSizeExceedsLimit()
            throws Exception {

        mockMvc.perform(
                        get("/v1/book")
                                .param("size", "101")
                )
                .andExpect(status().isBadRequest());
    }
}