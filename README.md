# Hospital Management System

A Java-based Hospital Management System built with Java 17, MySQL, JDBC, Maven, and Java Swing.

The application provides role-based workflows for administrators, doctors, and receptionists to manage patients, staff, appointments, and billing. It includes both a console-based interface and a graphical Swing interface.

## Features

### Admin

- Admin authentication
- Add and view doctors
- Add and view receptionists
- View patients
- Staff management

### Doctor

- Doctor authentication
- View assigned appointments
- View assigned patients
- View patient details
- Search doctors by specialization
- Search available doctors
- Mark appointments as completed

### Receptionist

- Receptionist authentication
- Add patients
- Schedule appointments
- View appointments
- View patients
- Generate bills

### Patient Management

- Patient personal details
- Illness/condition information
- Admission status and date
- Assigned doctor
- Billing information

### Appointments

- Schedule appointments
- View appointments
- Filter appointments by doctor or patient
- Cancel appointments
- Mark appointments as completed

### Billing

- Create and view bills
- Update and delete bills
- Calculate billing amounts using doctor consultation fees
- Process payments in memory

## Technical Structure

The project follows a layered architecture:

```text
Entities
   ↓
Services
   ↓
DAOs
   ↓
MySQL Database
```

- **Entities** represent the application's domain objects.
- **Services** contain business logic.
- **DAOs** handle database operations.
- **DBConnection** manages the JDBC connection.
- **Interfaces** such as `Billable` and `Schedulable` define shared behaviour.

The project also demonstrates object-oriented concepts including inheritance:

```text
Person
  └── Staff
       ├── Admin
       ├── Doctor
       └── Receptionist
```

## Technologies

- Java 17
- Maven
- Java Swing / AWT
- JDBC
- MySQL
- MySQL Connector/J
- JUnit

## Requirements

Before running the project, install:

- JDK 17
- Maven
- MySQL Server

MySQL should be running on port `3306`.

The application expects a database named:

```text
hms
```

The required tables are:

- `admins`
- `doctors`
- `receptionists`
- `patients`
- `appointments`
- `bills`

> The project currently does not include an SQL schema/setup script, so the database and required tables must be created separately before running the application.

## Database Configuration

Update the database connection details in:

```text
DBConnection.java
```

with your local MySQL username, password, and database configuration.

## Running the Application

### Console Version

Run:

```text
com.hms.App
```

The application opens a role-selection menu and provides separate workflows for administrators, doctors, and receptionists.

### Swing Version

Run:

```text
com.hms.AppSwing
```

This launches the graphical desktop interface.

### Database Connection Test

You can also run:

```text
com.hms.DBConnection
```

to test the MySQL connection.

## Sample Accounts

The application seeds sample accounts during startup.

| Role         | Username    | Password    |
| ------------ | ----------- | ----------- |
| Admin        | `ad401un`   | `ad401un`   |
| Doctor       | `docCar1un` | `docCar1un` |
| Receptionist | `rec281un`  | `rec281un`  |

These accounts are intended for local/demo use.

## Project Structure

```text
hms/
├── dao/
├── entities/
├── services/
├── interfaces/
├── utils/
├── App.java
├── AppSwing.java
└── DBConnection.java

pom.xml
```

## Testing

The project includes a basic JUnit test setup.

## Status

Completed academic project demonstrating:

- Java OOP
- Inheritance and interfaces
- Layered application architecture
- JDBC database connectivity
- MySQL CRUD operations
- Authentication
- Java Swing GUI development
- Maven project management
