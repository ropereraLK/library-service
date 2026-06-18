package io.ropereralk.github.library.contoller.v1;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ropereralk.github.library.contoller.BorrowerController;
import io.ropereralk.github.library.dto.request.BorrowerRequest;
import io.ropereralk.github.library.dto.response.BorrowerResponse;
import io.ropereralk.github.library.service.BorrowerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BorrowerController.class)
class BorrowerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BorrowerService borrowerService;

    @Test
    void shouldCreateBorrower() throws Exception {

        BorrowerRequest request =
                new BorrowerRequest(
                        "Rohan Perera",
                        "rohan@example.com");

        BorrowerResponse response =
                new BorrowerResponse(
                        1L,
                        "Rohan Perera",
                        "rohan@example.com");

        when(borrowerService.createBorrower(any()))
                .thenReturn(response);

        mockMvc.perform(
                        post("/v1/members")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Rohan Perera"))
                .andExpect(jsonPath("$.email").value("rohan@example.com"));
    }

    @Test
    void shouldReturnBadRequestWhenEmailIsInvalid()
            throws Exception {

        BorrowerRequest request =
                new BorrowerRequest(
                        "Rohan",
                        "invalid-email");

        mockMvc.perform(post("/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper
                                .writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}