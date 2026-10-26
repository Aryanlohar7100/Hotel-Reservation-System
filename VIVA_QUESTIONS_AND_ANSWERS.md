# 🎓 StayEase — Comprehensive Viva Preparation Guide & Project Reference

> **Project Name:** StayEase Hotel Reservation Management System  
> **Platform Type:** Enterprise Full-Stack Web Application  
> **Target Audience:** External Examiners, Technical Interviewers, Project Evaluators, Viva Voce Panels  

---

## 📌 1. Project Overview & 30-Second Elevator Pitch

> *"StayEase is an enterprise-grade full-stack hotel reservation platform built with **Java 21**, **Spring Boot 3.3.4**, **Spring Data JPA/Hibernate**, **Spring Security 6**, and **MySQL 8** (with an instant **H2 in-memory** demo fallback). It enables travelers to discover hotels across destinations, verify real-time room availability across date ranges, book rooms without double-booking risk, receive digital receipts with unique booking references, and manage cancellations with automated refund processing. It also features a hospitality management admin dashboard for live occupancy analytics, revenue tracking, and reservation status transitions."*

---

## 🛠️ 2. Complete Technology Stack & Version Matrix

| Layer | Technology | Version | Purpose & Rationale |
|---|---|---|---|
| **Language** | **Java** | **21 (LTS)** | Modern language features (Records, Pattern Matching, Virtual Threads readiness, enhanced switch expressions). |
| **Backend Framework** | **Spring Boot** | **3.3.4** | Rapid application bootstrap, convention over configuration, embedded Tomcat 10 container, dependency injection. |
| **ORM / Data Access** | **Spring Data JPA / Hibernate** | **6.x** | Object-Relational Mapping, automated repository generation, JPQL queries, dirty checking, transactional boundary management. |
| **Connection Pool** | **HikariCP** | **Spring Boot Default** | Industry's fastest, lightweight high-concurrency JDBC connection pool. |
| **Security & Auth** | **Spring Security** | **6.x** | Servlet filter chain security, BCrypt password hashing, Role-Based Access Control (`ROLE_CUSTOMER`, `ROLE_ADMIN`). |
| **Validation** | **Jakarta Bean Validation (Hibernate Validator)** | **3.0** | Server-side contract enforcement (`@NotNull`, `@NotBlank`, `@Email`, `@Min`, `@Valid`). |
| **Production Database** | **MySQL Server** | **8.0+** | ACID-compliant relational persistence, foreign key integrity, index optimizations. |
| **Dev / Fallback DB** | **H2 Database** | **2.2+** | Zero-configuration in-memory SQL database for instant portability, demonstrations, and automated testing. |
| **Frontend Structure** | **HTML5 Semantic Markup** | **HTML5** | Accessible layout, SEO meta tags, data attributes for dynamic DOM binding. |
| **Frontend Styling** | **Bootstrap 5.3 & Custom CSS** | **5.3.3** | Responsive 12-column grid, modals, offcanvas, toasts, luxury hospitality palette (Navy/Gold/Cream). |
| **Frontend Logic** | **Vanilla JavaScript (ES6+)** | **ECMAScript 2022** | Async/Await, Fetch API, DOM manipulation, state management via SessionStorage/LocalStorage. No bulky frontend node dependencies needed. |
| **Build & Dependency Tool**| **Apache Maven** | **3.9+ (`./mvnw`)**| Deterministic dependency resolution, build lifecycle management, executable fat JAR generation. |
| **Testing** | **JUnit 5 (Jupiter) & Mockito** | **5.x / 5.x** | Unit testing service isolation, mock repositories, asserting business rules and boundary exceptions. |

---

## 🏛️ 3. System Architecture & Design Patterns

### Architectural Style: **Layered (3-Tier) Architecture**

```
Browser Client (HTML5 / Bootstrap 5 / ES6 Fetch)
       │ (JSON via HTTP REST)
       ▼
Controllers Layer (`@RestController`, e.g. BookingController, HotelController)
       │ (DTOs - Request / Response)
       ▼
Service Layer (`@Service`, Business Logic, `@Transactional`, validations)
       │ (Domain Entities)
       ▼
Repository Layer (`@Repository`, Spring Data JPA interfaces, JPQL Queries)
       │ (SQL via HikariCP)
       ▼
Database (MySQL 8 / H2 In-Memory Database)
```

### Key Design Patterns Implemented:
1. **Controller-Service-Repository Pattern**: Strict separation of concerns — controllers handle HTTP semantics, services enforce business rules, and repositories isolate persistence.
2. **Data Transfer Object (DTO) Pattern**: Decouples internal database entities (`User`, `Room`, `Booking`) from public API payloads (`BookingRequest`, `BookingResponse`), preventing mass assignment vulnerabilities and infinite recursion in bi-directional JPA relationships.
3. **Repository Pattern**: Abstracts database queries using Spring Data JPA interfaces without writing boilerplate JDBC code.
4. **Singleton Pattern**: Spring-managed beans (`@Service`, `@Repository`, `@RestController`, `PasswordEncoder`) are initialized as singletons within the ApplicationContext.
5. **Dependency Injection / Inversion of Control (IoC)**: Constructor-based injection used throughout the application for testability, immutability, and circular dependency avoidance.
6. **Strategy Pattern (Authentication)**: `DaoAuthenticationProvider` works with `CustomUserDetailsService` and `PasswordEncoder` to validate credentials.
7. **Global Exception Handling (Front Controller / Interceptor pattern)**: `@RestControllerAdvice` captures application-wide exceptions and translates them into uniform JSON responses (`ApiResponse<T>`).

---

## 🗄️ 4. Database Schema & Entity Relationships

The relational model consists of **7 core tables** designed with 3rd Normal Form (3NF) principles:

```
┌──────────────┐          ┌──────────────┐          ┌──────────────┐
│    users     │ 1      * │   bookings   │ *      1 │    hotels    │
│──────────────│──────────│──────────────│──────────│──────────────│
│ id (PK)      │          │ id (PK)      │          │ id (PK)      │
│ email (UQ)   │          │ user_id (FK) │          │ name         │
│ password     │          │ hotel_id(FK) │          │ city         │
│ role         │          │ check_in     │          │ star_rating  │
└──────────────┘          │ check_out    │          └──────┬───────┘
                          │ status       │                 │ 1
                          └──────┬───────┘                 │
                                 │ 1                       │ *
                                 │                  ┌──────┴───────┐
                          ┌──────┴───────┐          │    rooms     │
                          │   payments   │          │──────────────│
                          │──────────────│          │ id (PK)      │
                          │ id (PK)      │          │ hotel_id(FK) │
                          │ booking_id(FK│          │ room_type    │
                          │ txn_id (UQ)  │          │ price_night  │
                          │ status       │          └──────┬───────┘
                          └──────────────┘                 │ 1
                                                           │ *
                          ┌──────────────┐          ┌──────┴───────┐
                          │   reviews    │          │ booking_items│
                          │──────────────│          │──────────────│
                          │ id (PK)      │          │ id (PK)      │
                          │ user_id (FK) │          │booking_id(FK)│
                          │ hotel_id(FK) │          │ room_id (FK) │
                          └──────────────┘          └──────────────┘
```

### Table Relationships:
- **`User` 1 ── * `Booking`**: One user can make many bookings (`@ManyToOne` in `Booking`).
- **`Hotel` 1 ── * `Room`**: One hotel has many physical room inventories (`@OneToMany` with orphan removal).
- **`Hotel` * ── * `Amenity`**: Hotels and amenities share a Many-to-Many join table `hotel_amenities`.
- **`Booking` 1 ── * `BookingItem`**: One booking contains line items specifying the reserved room, nights, and rate snapshot (`@OneToMany(cascade = ALL)`).
- **`Booking` 1 ── 1 `Payment`**: Every booking links to exactly one payment transaction (`@OneToOne(cascade = ALL)`).
- **`Hotel` 1 ── * `Review`** & **`User` 1 ── * `Review`**: Reviews link a specific customer to a reviewed hotel (`@ManyToOne`).

---

## ⚡ 5. Core Algorithms & Critical Business Logic

### Q: How is double-booking prevented? (The Overlapping Interval Query)
When a user attempts to book a room between `checkInDate` and `checkOutDate`, the system checks whether any existing **active (non-cancelled)** reservation for that room overlaps:

```java
@Query("SELECT COUNT(r) = 0 FROM Room r " +
       "WHERE r.id = :roomId " +
       "AND r.id IN (" +
       "   SELECT bi.room.id FROM BookingItem bi " +
       "   WHERE bi.booking.bookingStatus <> com.stayease.entity.BookingStatus.CANCELLED " +
       "   AND bi.booking.checkInDate < :checkOutDate " +
       "   AND bi.booking.checkOutDate > :checkInDate" +
       ")")
boolean isRoomAvailable(@Param("roomId") Long roomId,
                        @Param("checkInDate") LocalDate checkInDate,
                        @Param("checkOutDate") LocalDate checkOutDate);
```

#### Why `checkInDate < :checkOutDate AND checkOutDate > :checkInDate`?
Two date intervals $[A_1, A_2)$ and $[B_1, B_2)$ overlap if and only if:
$$\text{Existing Check-In} < \text{Requested Check-Out} \quad \text{AND} \quad \text{Existing Check-Out} > \text{Requested Check-In}$$
This correctly allows a new guest to check in on the **same day** that a previous guest checks out (e.g. Guest A leaves Oct 15, Guest B arrives Oct 15).

### Q: How does the system handle concurrent simultaneous bookings?
1. **Isolation Level:** The booking creation method is annotated with `@Transactional(isolation = Isolation.REPEATABLE_READ)`.
2. **Synchronization:** The method is marked `synchronized` at the service layer to serialize booking requests at the thread level.
3. **Pre-commit Re-verification:** Even after calculating the price, the availability query is checked immediately before persisting the booking. If two requests race, the second detects unavailability and throws `RoomUnavailableException (HTTP 409 Conflict)`.

### Q: Why is `BigDecimal` used for currency instead of `float` or `double`?
In Java, `float` and `double` use IEEE-754 binary floating-point representation, which causes imprecise fractions (e.g., `0.1 + 0.2 = 0.30000000000000004`). In financial and hospitality systems, accumulating tiny rounding discrepancies causes lost revenue or audit failures. `BigDecimal` provides exact decimal representation and control over rounding modes.

---

## 🔐 6. Security & Authentication Deep-Dive

### Q: How does authentication and authorization work in StayEase?
- **Password Security:** Passwords are never stored in plain text. Spring Security's `BCryptPasswordEncoder` hashes passwords using a salted key-derivation function with work factor 10. BCrypt incorporates a random salt directly into the output hash, protecting against rainbow table attacks.
- **Authentication Provider:** `DaoAuthenticationProvider` intercepts credentials, delegates user lookups to `CustomUserDetailsService`, loads user authorities (`ROLE_CUSTOMER` or `ROLE_ADMIN`), and compares the password hash.
- **Session / Transport Security:** HTTP Basic and authenticated session cookies allow stateless REST calls from client-side JavaScript.
- **RBAC (Role-Based Access Control):**
  - **Public:** Viewing hotels, searching rooms, viewing reviews, registering, logging in.
  - **ROLE_CUSTOMER:** Booking a room, viewing personal bookings (`/api/bookings/my-bookings`), cancelling personal bookings, submitting reviews.
  - **ROLE_ADMIN:** Creating/updating/deleting hotels and rooms, viewing administrative occupancy analytics (`/api/admin/stats`), inspecting all customer bookings, overriding booking statuses.

---

## ❓ 7. Top 35 Viva Questions & Model Answers

### Category A: General & Architecture

#### 1. What problem does the StayEase application solve?
**Answer:** It eliminates manual hotel reservation paperwork, prevents costly double-booking errors across busy dates, provides real-time room inventory availability, automates price and tax calculations, and provides management with occupancy and revenue metrics.

#### 2. Why did you choose Spring Boot instead of standard Spring MVC or plain Java EE?
**Answer:** Spring Boot provides opinionated pre-configured starters (`spring-boot-starter-web`, `spring-boot-starter-data-jpa`), an embedded Tomcat container (no need to deploy WARs to external servers like WildFly), automatic dependency management, and production-ready monitoring.

#### 3. What is the difference between a Monolithic and Microservice architecture, and which one is this?
**Answer:** StayEase is designed as a **Modular Monolith**. All modules (Auth, Hotels, Rooms, Bookings, Payments, Reviews) share a single deployable artifact and unified database. This avoids network latency, distributed transaction complexity (2-phase commit/Sagas), and DevOps overhead while maintaining high cohesion through distinct service layers.

#### 4. What is a DTO and why do you use it instead of returning JPA entities directly?
**Answer:** DTO stands for Data Transfer Object. Returning entities directly causes three major issues:
1. **Security risk:** Entities may leak sensitive fields like hashed passwords.
2. **Infinite JSON recursion:** Bi-directional relationships (e.g., Hotel $\leftrightarrow$ Room) cause `StackOverflowError` during Jackson serialization.
3. **Over-fetching:** DTOs allow sending only the exact fields required by the UI view.

---

### Category B: Java 21 & Backend Concepts

#### 5. What are the key features of Java 21 that benefit Spring Boot?
**Answer:** Java 21 is a Long-Term Support (LTS) release bringing Virtual Threads (Project Loom), Record Patterns, Pattern Matching for `switch`, Sequenced Collections, and enhanced garbage collection (ZGC). It allows Spring Boot applications to scale to thousands of concurrent requests with minimal memory overhead.

#### 6. What is the difference between `@Component`, `@Service`, and `@Repository`?
**Answer:** All three are stereotypes derived from `@Component`:
- `@Repository` is for data access classes and automatically translates database-specific SQLExceptions into Spring's unified `DataAccessException` hierarchy.
- `@Service` denotes business logic and marks classes intended for transaction handling.
- `@Component` is a generic Spring-managed bean annotation.

#### 7. How does Spring's Inversion of Control (IoC) and Dependency Injection (DI) work?
**Answer:** Instead of classes using `new` to instantiate dependencies (e.g., `new BookingRepository()`), the Spring IoC Container discovers classes via `@Service`/`@Repository`, instantiates them as singleton beans at startup, and injects them via constructor injection where needed.

#### 8. Why is Constructor Injection preferred over Field Injection (`@Autowired` on fields)?
**Answer:**
1. Allows dependencies to be declared `final`, making the bean immutable and thread-safe.
2. Simplifies unit testing because dependencies can be passed via constructor without reflection or Spring test runners.
3. Fails fast at compile or startup time if required dependencies are missing.

---

### Category C: Database, JPA & Hibernate

#### 9. What is the difference between JPA and Hibernate?
**Answer:** JPA (Jakarta Persistence API) is a specification/interface standard. Hibernate is the actual ORM library implementation that executes the JPA standard.

#### 10. What does `spring.jpa.hibernate.ddl-auto=update` do?
**Answer:** It instructs Hibernate to inspect the JPA `@Entity` classes at startup, compare them with the existing database schema, and automatically create missing tables or add missing columns without dropping existing data.

#### 11. What is the N+1 select problem in Hibernate, and how did you prevent it?
**Answer:** The N+1 problem occurs when fetching an entity with lazy relationships triggers 1 query for the parent, plus N additional queries for each child. In StayEase, we use custom JPQL queries with joins and projection DTOs so data is retrieved in a single efficient SQL query.

#### 12. Explain Cascade types in JPA. What does `CascadeType.ALL` on `Booking.items` do?
**Answer:** Cascade specifies whether entity lifecycle operations (persist, merge, remove) should cascade to related children. When a `Booking` is saved, `CascadeType.ALL` automatically persists its `BookingItem` and `Payment` records in the same transaction without calling separate repository saves.

#### 13. What is the purpose of H2 in your project when MySQL is also configured?
**Answer:** MySQL is the primary production database. H2 serves as an instant fallback and demo profile (`--spring.profiles.active=h2`). It allows evaluators or examiners to run the complete system immediately on any computer with zero configuration, without needing to install, configure, or start a local MySQL service.

---

### Category D: Concurrency, Bookings & Business Logic

#### 14. What happens when a customer cancels a booking?
**Answer:**
1. The system verifies that the requesting user owns the booking (or is an Admin).
2. The booking status updates to `CANCELLED`.
3. The associated payment status automatically switches to `REFUNDED`.
4. The room inventory immediately becomes available for other customers because the availability JPQL query filters out bookings where `status = CANCELLED`.

#### 15. What does `@Transactional` do on the `createBooking` method?
**Answer:** It ensures the entire booking operation (date verification, capacity check, availability check, room item creation, payment generation, and database commit) behaves as a single Atomic unit (ACID). If an error or exception occurs at any point, all changes roll back, preventing half-created bookings or orphaned payment records.

#### 16. What is the difference between `@RequestParam` and `@PathVariable`?
**Answer:**
- `@PathVariable` extracts values directly from the URI path (e.g. `/api/bookings/{id}` $\rightarrow$ `Long id`).
- `@RequestParam` extracts query string parameters (e.g. `/api/rooms/available?hotelId=1&checkIn=2026-11-01` $\rightarrow$ `hotelId`, `checkIn`).

#### 17. How is the booking reference generated?
**Answer:** The reference is generated as `STY-{YEAR}-{RANDOM_5_ALPHANUMERIC}` (e.g., `STY-2026-A9F3K`). This gives customers a professional, human-readable confirmation code that is indexed for quick receipt lookups.

---

### Category E: Security & Web Configuration

#### 18. Why was CSRF disabled in your REST API security configuration?
**Answer:** Cross-Site Request Forgery (CSRF) protection is designed for traditional session-cookie form submissions in server-rendered applications (like Spring MVC JSP/Thymeleaf). For stateless REST APIs where requests are initiated via JavaScript Fetch using HTTP headers and authorization tokens, CSRF tokens can be safely disabled.

#### 19. How does BCrypt differ from MD5 or SHA-256?
**Answer:** MD5 and SHA-256 are fast general-purpose cryptographic hash functions vulnerable to brute-force and rainbow table attacks. BCrypt is an adaptive key-derivation function based on the Blowfish cipher. It is intentionally slow, utilizes a salt automatically embedded within the hash, and allows increasing the iteration cost factor as computer hardware improves.

#### 20. What is a 401 Unauthorized vs 403 Forbidden HTTP status?
**Answer:**
- `401 Unauthorized`: The client has not authenticated (not logged in) or credentials are invalid.
- `403 Forbidden`: The client is authenticated, but does not have the required role or authority to access the resource (e.g., a Customer trying to delete a Hotel).

---

### Category F: Frontend & Integration

#### 21. How does the frontend communicate with the backend?
**Answer:** The frontend uses the browser's native `fetch()` API to make asynchronous HTTP requests (`GET`, `POST`, `PUT`, `DELETE`) with JSON headers. When responses return, JavaScript dynamically constructs and injects DOM nodes without refreshing the page.

#### 22. Where are user credentials or session state stored on the client side?
**Answer:** Authenticated user information (name, email, role, and Basic Auth token) is saved in the browser's `localStorage` or `sessionStorage`. When the user logs out, the storage is cleared, and the UI redirects to the home page.

#### 23. What are the key frontend pages included in the system?
**Answer:**
1. `index.html`: Hero discovery, destination search, featured hotels, amenity highlights.
2. `hotels.html`: Search results with dynamic filters (city, star rating, max price, sort).
3. `hotel-detail.html`: Property details, amenities, room types, reviews, and direct booking CTA.
4. `booking.html`: Guest details, date selection, real-time night and price summary, payment selector.
5. `booking-confirmation.html`: Digital receipt voucher with reference code and printable receipt.
6. `customer-dashboard.html`: Past and upcoming reservations with instant cancellation button.
7. `admin-dashboard.html`: Live business KPIs (revenue, occupancy rate, total bookings), reservation status manager, and property inventory list.
8. `login.html` & `register.html`: Secure authentication forms with client and server validation.

---

### Category G: Testing & Quality Assurance

#### 24. What types of automated tests did you write?
**Answer:** We wrote unit tests using JUnit 5 and Mockito in `BookingServiceTest` and `AuthServiceTest`. They verify:
- Happy path booking creation with valid dates and guests.
- Rejection of double bookings on conflicting dates (`RoomUnavailableException`).
- Rejection of invalid dates (check-in in the past, checkout before check-in).
- Rejection of bookings exceeding room capacity.
- Proper cancellation and refund status transitions.
- User registration and duplicate email rejection.

#### 25. How do you run the test suite?
**Answer:** Using the Maven wrapper command:
```bash
./mvnw test
```

---

## 📋 8. Step-by-Step Live Demo Script for the Viva Panel

If asked to demonstrate the project live, follow this structured 5-minute workflow:

1. **Start the Application:**
   ```bash
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=h2
   ```
   *Explain: "I am starting the system with the pre-seeded H2 profile so you can see live data instantly without any external database dependency."*

2. **Open the Homepage:**
   - Navigate to `http://localhost:8080/`
   - Show the responsive navbar, search bar, and pre-loaded luxury hotels (e.g., The Leela Palace, Taj Lake Palace).

3. **Demonstrate Customer Search & Room Filtering:**
   - Search for **"New Delhi"** or click on **The Leela Palace**.
   - Show how room types (Deluxe, Presidential Suite), capacities, amenities, and nightly prices are loaded dynamically via REST API.

4. **Complete a Real Booking:**
   - Click **Book Now** on a room.
   - Log in using customer credentials: `customer@stayease.com` / `Customer@123`.
   - Select check-in (tomorrow) and check-out (3 days later).
   - Point out: *"Notice how the total nights and total amount are calculated using database-backed pricing via BigDecimal."*
   - Select a payment method and click **Confirm Reservation**.

5. **Show the Digital Confirmation Voucher:**
   - Highlight the unique reference code (e.g. `STY-2026-XXXXX`), status `CONFIRMED`, and payment status `SUCCESS`.

6. **Demonstrate Double-Booking Prevention:**
   - Open a new tab or incognito window, select the **same room for the same dates**, and attempt to book it.
   - Point out: *"The system immediately blocks the transaction with an error indicating the room is no longer available."*

7. **Demonstrate Cancellation & Inventory Recovery:**
   - Open **Customer Dashboard** (`customer-dashboard.html`).
   - Click **Cancel Reservation**.
   - Show that status changes to `CANCELLED` and refund status to `REFUNDED`.
   - Show that the room dates are now available again for booking.

8. **Demonstrate the Hospitality Admin Portal:**
   - Log out and log in as Admin: `admin@stayease.com` / `Admin@123`.
   - Navigate to `/admin-dashboard.html`.
   - Point out live metrics: Total Properties, Total Bookings, Total Gross Revenue, and Occupancy Rate.

---

## 🎯 9. Quick Cheat Sheet: Important Code Locations

- **Main Application Class:** `src/main/java/com/stayease/StayEaseApplication.java`
- **Security & RBAC Config:** `src/main/java/com/stayease/config/SecurityConfig.java`
- **Double Booking Query:** `src/main/java/com/stayease/repository/RoomRepository.java` (Method `isRoomAvailable`)
- **Booking Engine & Business Logic:** `src/main/java/com/stayease/service/impl/BookingServiceImpl.java`
- **Database Initializer:** `src/main/java/com/stayease/config/DataSeeder.java`
- **Global Error Handler:** `src/main/java/com/stayease/exception/GlobalExceptionHandler.java`
- **Frontend Pages:** `src/main/resources/static/*.html`
- **Unit Tests:** `src/test/java/com/stayease/service/BookingServiceTest.java`
