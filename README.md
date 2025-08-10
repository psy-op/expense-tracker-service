Expense Tracker Service
=======================

Spring Boot REST API for tracking personal and business expenses. Supports user authentication, expense categorization, budget limits, and monthly reports with CSV export. Includes JWT security, validation, pagination, and Swagger (OpenAPI) documentation.

Stack
-----
* Java 17
* Spring Boot 3
* Spring Web, Spring Data JPA, Spring Security
* JWT (jjwt)
* PostgreSQL (H2 in-memory for dev/test)
* Lombok
* springdoc-openapi
* JUnit 5 + MockMvc

Run (Dev / H2)
--------------
1. Ensure Java 17+ installed.
2. Set optional env var for JWT secret (strong value recommended):
	 Windows PowerShell: `$env:APP_JWT_SECRET = "your_long_secret_value"`
3. Package & run:
```
mvn spring-boot:run
```
4. Swagger UI: http://localhost:8080/swagger-ui/index.html
5. H2 Console: http://localhost:8080/h2-console (JDBC URL: `jdbc:h2:mem:expensetracker`)

Switch to PostgreSQL
--------------------
Provide a running PostgreSQL instance then run:
```
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```
Adjust `spring.datasource.*` in `application.yml` if needed.

Auth Flow
---------
`POST /api/auth/register` -> returns `{ token }`  (auto login upon registration)

`POST /api/auth/login` -> returns `{ token }`

Include header: `Authorization: Bearer <token>` for protected endpoints.

Core Endpoints (Current)
------------------------
* POST /api/expenses
* GET /api/expenses (filters: categoryId, startDate, endDate, minAmount, maxAmount, pagination + sort)
* PUT /api/expenses/{id}
* DELETE /api/expenses/{id}
* GET /api/expenses/export (CSV)
* POST /api/budgets
* GET /api/budgets/current
* GET /api/reports/monthly (optional query params month, year)

Planned / TODO
--------------
* Email notification when budget exceeds limit
* Recurring expenses
* Per-user custom categories CRUD endpoint
* Enhanced error handling & problem details
* Refresh tokens & logout support
* More comprehensive tests (services & edge cases)

Project Structure (Key)
----------------------
```
src/main/java/com/example/expensetracker
	config/ (security + data init)
	controller/ (REST controllers)
	dto/ (request/response objects)
	model/ (JPA entities)
	repository/ (Spring Data interfaces)
	security/ (JWT support)
	service/ (business logic)
	util/ (CSV helper)
```

Testing
-------
```
mvn test
```

License
-------
MIT (add file if needed)
# expense-tracker-service
Spring Boot REST API for tracking personal and business expenses. Supports user authentication, expense categorization, budget limits, and monthly reports with CSV export. Includes JWT security, validation, pagination, and Swagger API documentation.
