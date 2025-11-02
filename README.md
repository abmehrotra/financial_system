# Customer Account & Transactions


Simple Spring Boot application that manages customer accounts and transactions.


## Tech stack
- Java 17
- Spring Boot
- Spring Data JPA (H2 in-memory database)
- SpringDoc OpenAPI (Swagger)
- JUnit 5, Mockito, AssertJ
- Maven
- Docker


## Run locally


1. Build


```bash
mvn clean package
```


2. Run


```bash
java -jar target/financial_system-1.0-SNAPSHOT.jar
```


API will be available at `http://localhost:8080/api/v1`.


Swagger UI: `http://localhost:8080/swagger-ui.html`
API docs: `http://localhost:8080/api-docs`


## Run tests


```bash
mvn test
```


## Docker


Build image:


```bash
docker build -t financial-system .
```


Run (compose):


```bash
docker-compose up --build
```


## Notes
- The project uses H2 by default. To switch to an external DB, update `application.yml` datasource properties.
- Environment variables: `PORT` can override server port.


## Troubleshooting
- If Swagger UI not visible, confirm `springdoc` dependencies are on the classpath and application started successfully.