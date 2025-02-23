# SplitWise - Expense Management System

SplitWise is a Spring Boot-based application that mimics the functionality of the popular expense-sharing app Splitwise. It allows users to create groups, add expenses, and generate expense reports. The application uses PostgreSQL as its primary database and implements **Spring AOP** to route read queries to a read-only database. Additionally, it uses **Resilience4j** for retry and circuit breaker logic to ensure fault tolerance and resilience.

## Features

1. **User Management**:
   - Create new users.
   - Delete users (automatically removes them from all groups).

2. **Group Management**:
   - Create groups between users.
   - Add expenses to a group or between two users.

3. **Expense Management**:
   - Add expenses with details like amount, description, and participants.
   - Generate expense reports for individual users or groups.

4. **Database Routing with Spring AOP**:
   - Uses Spring AOP to route read queries to a read-only database.
   - Write queries are directed to a read/write database, which syncs with the read-only database.

5. **Resilience4j for Fault Tolerance**:
   - Implements **retry logic** to handle transient failures (e.g., database connection issues).
   - Uses **circuit breaker** to prevent cascading failures and degrade gracefully during high load or failures.

## Technologies Used

- **Backend**: Spring Boot, Spring JDBC, Spring AOP
- **Database**: PostgreSQL
- **Resilience**: Resilience4j (Retry, Circuit Breaker)
- **Build Tool**: Maven
- **Other**: Java, RESTful APIs

## Database Routing with Spring AOP

The application uses Spring AOP to create a custom annotation (`@ReadOnly`) that routes read queries to a read-only database. This ensures:
- The read-only database handles all read queries.
- Write queries are directed to the read/write database, which syncs with the read-only database.

This approach improves performance by distributing the load between read and write databases.

## Resilience4j for Fault Tolerance

The application uses **Resilience4j** to handle transient failures and improve system resilience:
- **Retry Logic**:
  - Automatically retries failed operations (e.g., database queries) a configurable number of times.
  - Helps recover from transient issues like network glitches or temporary database unavailability.
- **Circuit Breaker**:
  - Prevents cascading failures by stopping requests to a failing service after a threshold is reached.
  - Degrades gracefully by returning fallback responses during high load or failures.

## Setup Instructions

### Prerequisites

1. **Java Development Kit (JDK)**: Ensure you have JDK 17 or higher installed.
2. **PostgreSQL**: Install and set up PostgreSQL on your machine.
3. **IDE**: Use IntelliJ IDEA, Eclipse, or any other Java IDE.

### Steps to Run the Project

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/VaibhavKaushik2205/SplitWise.git
   cd SplitWise
   ```

2, **Set Up PostgreSQL**:

Create two databases named splitwise-rw (read-write)/ splitwise-ro (read-only).

Update the application.properties file with your PostgreSQL credentials:


```
#database details for READ_WRITE_DB
spring.datasource.read-write.url=${DATASOURCE_URL:jdbc:postgresql://localhost:5432/splitwise-rw}
spring.datasource.read-write.username=${DATASOURCE_USERNAME:your_username}
spring.datasource.read-write.password=${DATASOURCE_PASSWORD:your_password}

#database details for READ_ONLY
spring.datasource.read-only.url=${DATASOURCE_URL:jdbc:postgresql://localhost:5432/splitwise-ro}
spring.datasource.read-only.username=${DATASOURCE_USERNAME:your_username}
spring.datasource.read-only.password=${DATASOURCE_PASSWORD:your_password}
```

**Build the Project**:
```
mvn clean install
```

**Run the Application**:
```
mvn spring-boot:run
```

The application will be available at http://localhost:8080.
