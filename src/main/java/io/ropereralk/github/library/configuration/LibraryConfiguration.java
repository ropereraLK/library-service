package io.ropereralk.github.library.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Library Management API",
                version = "1.0.0",
                description = "REST API for managing books, borrowers, book copies and borrowing operations",
                contact = @Contact(
                        name = "Rohan Perera"
                )
        )
)
public class LibraryConfiguration {
}
