# Contributing to StayEase Hotel Reservation System

Thank you for your interest in contributing to **StayEase**! We welcome contributions to make this enterprise reservation system even better.

---

## 🛠️ Development Setup

1. **Fork and Clone the Repository:**
   ```bash
   git clone https://github.com/Aryanlohar7100/Hotel-Reservation-System.git
   cd Hotel-Reservation-System
   ```

2. **Prerequisites:**
   - **Java Development Kit (JDK):** Version 21 or later
   - **Maven:** 3.9+
   - **MySQL:** 8.0+ (Optional — In-memory H2 profile runs with zero configuration)

3. **Run with H2 (Instant Local Testing):**
   ```bash
   mvn spring-boot:run -Dspring-boot.run.profiles=h2
   ```

4. **Run Unit Tests:**
   ```bash
   mvn clean test
   ```

---

## 🔀 Branching Strategy & Pull Requests

1. Create a feature branch:
   ```bash
   git checkout -b feature/your-feature-name
   ```
2. Commit your changes following clear semantic messages:
   ```bash
   git commit -m "feat: add payment gateway webhook reconciliation"
   ```
3. Push to your fork:
   ```bash
   git push origin feature/your-feature-name
   ```
4. Open a Pull Request targeting `main`.

---

## 🧪 Code Quality Standards

- Maintain existing clean code conventions and design patterns.
- Ensure all automated unit tests (`mvn test`) pass before submitting.
- Do not commit secrets, passwords, or personal credentials.
- Adhere to Spring Boot best practices (Service Layer pattern, Repository pattern, DTO encapsulation).
