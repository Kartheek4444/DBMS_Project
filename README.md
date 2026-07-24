# Car Rental Management System 🚗💨

A full-stack enterprise Car Rental & Fleet Management Web Application built with **Spring Boot 3**, **Spring Security**, **Spring Data JPA**, **MySQL**, **Thymeleaf**, and **Tailwind CSS**.

---

## 🌟 Key Features

### 👤 User Roles & Authentication
- **Unified Authentication**: Unified login mechanism handling both Customers and Staff/Admin users with BCrypt password hashing.
- **Role-Based Access Control (RBAC)**:
  - **USER / Customer**: Browse vehicles, book rentals, view booking history, and manage customer profile.
  - **STAFF**: Oversee fleet maintenance, create & edit rental agreements, record vehicle pickup/return conditions, and manage active bookings.
  - **ADMIN**: Access system dashboard, onboard and manage staff members, control roles, and oversee all system operations.

### 🚘 Fleet & Vehicle Management
- **Vehicle Catalog**: Browse available vehicles with category filtering (SUV, Sedan, Luxury, Economy), status tracking (`AVAILABLE`, `RENTED`, `MAINTENANCE`), and price sorting.
- **Vehicle Registration & Edits**: Register new vehicles with image upload support, mileage tracking, and specification details.

### 📅 Booking & Overlap Validation
- **Smart Booking Flow**: Auto-calculates rental costs, collects deposit amounts, and validates booking date ranges (`returnDate > pickupDate`).
- **Double-Booking Prevention**: Automated conflict detection algorithm prevents double-booking vehicles that are already reserved or undergoing servicing.

### 📄 Rental Agreements & Condition Logs
- **Digital Rental Agreements**: Create and manage rental agreements linked to confirmed bookings.
- **Pickup & Return Tracking**: Log exact mileage, pickup/return dates, and vehicle condition descriptions during check-in/check-out.

### 🛠️ Fleet Maintenance
- **Maintenance Records**: Schedule, view, and record vehicle service logs, maintenance costs, description of repairs, and assigned staff members.
- **Detailed Maintenance Views**: Comprehensive breakdown of individual maintenance service records.

### 💳 Payment Processing
- **Payment Lifecycle**: Record and track deposit payments, total rental balances, and transaction statuses (`COMPLETED`, `PENDING`, `REFUNDED`).
- **REST APIs**: Full REST API endpoints for payment operations.

---

## 🛠️ Technology Stack

- **Backend**: Java 21, Spring Boot 3.5.6, Spring Security 6, Spring Data JPA, Hibernate, Lombok
- **Database**: MySQL 8.x
- **Frontend**: Thymeleaf, HTML5, Vanilla CSS, Tailwind CSS, FontAwesome 6
- **Build Tool**: Apache Maven (Wrapper included)

---

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed on your system:
- **Java Development Kit (JDK 21)** or higher
- **MySQL Server 8.0+**
- **Git**

### Database Setup

1. Open your MySQL client (e.g., MySQL Workbench, phpMyAdmin, or CLI) and create a database:
   ```sql
   CREATE DATABASE car_rental_db;
   ```

2. Update database credentials in `src/main/resources/application.properties` if necessary:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/car_rental_db
   spring.datasource.username=YOUR_MYSQL_USERNAME
   spring.datasource.password=YOUR_MYSQL_PASSWORD
   ```

### Running the Application

1. **Clone the repository**:
   ```bash
   git clone https://github.com/Kartheek4444/DBMS_Project.git
   cd car_rental
   ```

2. **Build and Run using Maven Wrapper**:

   - **Windows**:
     ```cmd
     .\mvnw.cmd spring-boot:run
     ```
   - **Linux / macOS**:
     ```bash
     ./mvnw spring-boot:run
     ```

3. **Access the application**:
   Open your browser and navigate to:
   ```
   http://localhost:8080
   ```

---

## 📂 Project Structure

```
car_rental/
├── src/main/java/com/dbmsproject/car_rental/
│   ├── controller/         # Web & REST Controllers (Booking, Customer, Staff, Vehicle, Maintenance, Payment)
│   ├── dto/                # Data Transfer Objects
│   ├── exception/          # Custom Exceptions & Resource Handlers
│   ├── mapper/             # Entity <-> DTO Mappers
│   ├── model/              # JPA Entities (Customer, Staff, Vehicle, Booking, RentalAgreement, Maintenance, Payment)
│   ├── repository/         # Spring Data JPA Repositories
│   ├── security/           # Spring Security Configuration & Custom UserDetailsService
│   └── service/            # Business Logic Interfaces & Service Implementations
├── src/main/resources/
│   ├── static/             # CSS, JavaScript, and Static Image Assets
│   ├── templates/          # Thymeleaf HTML Templates
│   └── application.properties # Application Configuration
├── uploads/                # Directory for uploaded vehicle and avatar images
├── pom.xml                 # Maven Dependencies & Build Setup
└── README.md
```

---

## 📌 API Endpoints Overview

| HTTP Method | Endpoint | Description | Access Level |
|---|---|---|---|
| `GET` | `/vehicles` | View vehicle catalog with filters | Public |
| `POST` | `/signup` | Register new customer account | Public |
| `POST` | `/login` | User & Staff Authentication | Public |
| `GET` | `/bookings/new` | Create booking form | USER, STAFF, ADMIN |
| `POST` | `/bookings/create` | Submit vehicle booking | USER, STAFF, ADMIN |
| `GET` | `/staff/dashboard` | Staff operation dashboard | STAFF, ADMIN |
| `GET` | `/admin/dashboard` | Administrative dashboard | ADMIN |
| `GET` | `/maintenance` | View fleet maintenance records | STAFF, ADMIN |
| `POST` | `/api/payments` | Process booking payment | Authenticated |
| `GET` | `/api/payments/booking/{bookingId}` | Fetch payment by booking ID | Authenticated |

---

## 🛡️ License

This project is developed as part of the DBMS Course Project.
