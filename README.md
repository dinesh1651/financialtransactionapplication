# Financial Transaction Processor

## Technology

- Java 17
- Spring Boot 3
- Spring Data JPA
- Hibernate
- H2 Database
- Maven
- JUnit5

## Features

- Batch Transaction Processing
- Async Processing
- ThreadPoolTaskExecutor
- Pessimistic Locking
- Duplicate Detection
- Validation using Java Streams
- Grouping using Collectors.groupingBy
- Transactional Processing

## API

POST

/api/v1/transactions/process

## Sample Request

[
 {
   "sourceAccountId":1,
   "targetAccountId":2,
   "amount":500,
   "timestamp":"2026-07-14T10:00:00"
 }
]

## Response

HTTP 202 Accepted

{
   "batchId":"xxxxxxxx",
   "message":"Batch accepted for processing"
}

## Run

mvn spring-boot:run
