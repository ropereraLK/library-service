# Library Service

A simple library management REST API built using Java 17 and Spring Boot.

### Features

Register borrowers
Register books and book copies
View all books
Borrow a book
Return a borrowed book
Swagger/OpenAPI documentation
Validation and exception handling
Unit and integration tests
Technology Stack
Java 17
Spring Boot 3
Spring Data JPA
PostgreSQL
Maven
JUnit 5
Mockito
JaCoCo
Swagger / OpenAPI

## Running the Application

Run the application using the local Spring profile:

-Dspring.profiles.active=local

or

mvn spring-boot:run -Dspring-boot.run.profiles=local
API Documentation

### Swagger UI:

http://localhost:8080/swagger-ui.html
Main Endpoints
Borrowers
POST /v1/borrowers

Create a new borrower.

Books
POST /v1/book
GET /v1/book

Register a book and retrieve all books.

Borrowing
POST /v1/borrow
POST /v1/borrow/return

Borrow and return book copies.

### Assumptions
Each physical book copy has a unique identifier.
Multiple copies of the same ISBN are supported.
A book copy can only be borrowed by one borrower at a time.
A borrower can borrow multiple books.
Authentication and authorization are out of scope for this exercise.
Testing

### Run all tests:

mvn clean verify

### JaCoCo report:

target/site/jacoco/index.html

Current coverage: approximately 96%.

### Database Choice

H2 is used for local development and testing for simplicity and fast startup.

PostgreSQL is used for higher environments as a production-ready relational database.

## Production Considerations

For simplicity, environment variables are used for database credentials.

In a production deployment, a centralized secret management solution such as AWS Secrets Manager is preferred.


### docker
cp .env.example .env
docker compose up --build