# Library Management System

A comprehensive Java-based Library Management System that demonstrates Object-Oriented Programming (OOP) principles, SOLID design principles, and relevant design patterns.

## Table of Contents
- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [Design Patterns](#design-patterns)
- [SOLID Principles](#solid-principles)
- [Class Diagram](#class-diagram)
- [Project Structure](#project-structure)
- [How to Run](#how-to-run)
- [Usage Examples](#usage-examples)
- [Technical Requirements](#technical-requirements)

## Overview

This Library Management System is designed to help librarians efficiently manage books, patrons, and lending processes. The system implements industry best practices and design patterns to ensure maintainability, extensibility, and robustness.

## Features

### Core Features
- **Book Management**: Add, remove, update, and search books by title, author, or ISBN
- **Patron Management**: Manage library members with different patron types and privileges
- **Lending Process**: Handle book checkout and return operations with due date tracking
- **Inventory Management**: Track available and borrowed books with real-time status updates
- **Fine Calculation**: Automatically calculate fines for overdue books

### Advanced Features
- **Reservation System**: Allow patrons to reserve books that are currently checked out
- **Notification System**: Email notifications for book availability and overdue reminders
- **Search Functionality**: Multiple search strategies for finding books
- **Borrowing History**: Complete tracking of patron borrowing patterns
- **Overdue Management**: Identification and management of overdue books

## Architecture

The system follows a layered architecture with clear separation of concerns:

1. **Model Layer**: Domain entities (Book, Patron, BorrowRecord, Reservation)
2. **Service Layer**: Business logic implementation (BookService, PatronService, LendingService)
3. **Pattern Layer**: Design pattern implementations (Strategy, Observer)
4. **Utility Layer**: Helper classes (Logger, IdGenerator)

## Design Patterns

### 1. Strategy Pattern
- **Implementation**: `SearchStrategy` interface with concrete strategies for different search types
- **Classes**: `TitleSearchStrategy`, `AuthorSearchStrategy`, `ISBNSearchStrategy`
- **Purpose**: Allows different search algorithms to be used interchangeably

### 2. Observer Pattern
- **Implementation**: `Subject` and `Observer` interfaces for notification system
- **Classes**: `EmailNotificationObserver`, `LendingServiceImpl`, `ReservationServiceImpl`
- **Purpose**: Enables loose coupling between notification triggers and notification handling

### 3. Facade Pattern
- **Implementation**: `LibraryManagementSystem` class provides a simplified interface
- **Purpose**: Hides the complexity of the subsystem and provides a unified interface

## SOLID Principles

### Single Responsibility Principle (SRP)
- Each class has a single, well-defined responsibility
- `Book` class only manages book-related data and operations
- `PatronService` only handles patron-related operations

### Open/Closed Principle (OCP)
- Classes are open for extension but closed for modification
- New search strategies can be added without modifying existing code
- New notification types can be added through the Observer pattern

### Liskov Substitution Principle (LSP)
- All implementations can be substituted for their interfaces
- Any `SearchStrategy` implementation can replace another
- Service implementations can be swapped without affecting client code

### Interface Segregation Principle (ISP)
- Interfaces are focused and don't force implementations to depend on unused methods
- Separate interfaces for `BookService`, `PatronService`, `LendingService`

### Dependency Inversion Principle (DIP)
- High-level modules don't depend on low-level modules
- Services depend on abstractions (interfaces) rather than concrete implementations

## Class Diagram

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     Book        │    │     Patron      │    │  BorrowRecord   │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ - isbn: String  │    │ - patronId      │    │ - recordId      │
│ - title: String │    │ - name: String  │    │ - patronId      │
│ - author: String│    │ - email: String │    │ - isbn: String  │
│ - status: Enum  │    │ - patronType    │    │ - borrowDate    │
├─────────────────┤    ├─────────────────┤    ├─────────────────┤
│ + borrowBook()  │    │ + canBorrow()   │    │ + isOverdue()   │
│ + returnBook()  │    │ + addRecord()   │    │ + getDaysOver() │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
         ┌─────────────────────────────────────────┐
         │         LibraryManagementSystem         │
         ├─────────────────────────────────────────┤
         │ - bookService: BookService              │
         │ - patronService: PatronService          │
         │ - lendingService: LendingService        │
         │ - reservationService: ReservationService│
         ├─────────────────────────────────────────┤
         │ + addBook()                             │
         │ + addPatron()                           │
         │ + borrowBook()                          │
         │ + returnBook()                          │
         │ + reserveBook()                         │
         └─────────────────────────────────────────┘
                                 │
         ┌───────────┬───────────┼───────────┬───────────┐
         │           │           │           │           │
   ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐
   │BookServ │ │PatronSv │ │LendingSv│ │ReservSv │ │Strategy │
   │ice      │ │ice      │ │ice      │ │ice      │ │Pattern  │
   └─────────┘ └─────────┘ └─────────┘ └─────────┘ └─────────┘
```

## Project Structure

```
src/
└── main/
    └── java/
        └── com/
            └── library/
                ├── models/
                │   ├── Book.java
                │   ├── BookStatus.java
                │   ├── Patron.java
                │   ├── PatronType.java
                │   ├── PatronStatus.java
                │   ├── BorrowRecord.java
                │   ├── Reservation.java
                │   └── ReservationStatus.java
                ├── services/
                │   ├── BookService.java
                │   ├── BookServiceImpl.java
                │   ├── PatronService.java
                │   ├── PatronServiceImpl.java
                │   ├── LendingService.java
                │   ├── LendingServiceImpl.java
                │   ├── ReservationService.java
                │   └── ReservationServiceImpl.java
                ├── patterns/
                │   ├── SearchStrategy.java
                │   ├── TitleSearchStrategy.java
                │   ├── AuthorSearchStrategy.java
                │   ├── ISBNSearchStrategy.java
                │   ├── Observer.java
                │   ├── Subject.java
                │   └── EmailNotificationObserver.java
                ├── utils/
                │   ├── Logger.java
                │   └── IdGenerator.java
                ├── LibraryManagementSystem.java
                └── LibraryDemo.java
```

## How to Run

### Prerequisites
- Java 17 or higher
- Any Java IDE (IntelliJ IDEA, Eclipse, VS Code)

### Steps
1. Clone or download the project
2. Navigate to the project directory
3. Compile the Java files:
   ```bash
   javac -d bin src/main/java/com/library/*.java src/main/java/com/library/*/*.java
   ```
4. Run the demo:
   ```bash
   java -cp bin com.library.LibraryDemo
   ```

### Using VS Code
1. Open the project folder in VS Code
2. Ensure Java Extension Pack is installed
3. Open `LibraryDemo.java`
4. Click "Run" or press F5

## Usage Examples

### Basic Operations

```java
// Create library system
LibraryManagementSystem library = new LibraryManagementSystem();

// Add books
library.addBook("978-0134685991", "Effective Java", "Ram halter", 2017);

// Add patrons
library.addPatron("STU001", "Suvi Pradhan", "abc@university.edu", 
                  "555-1234", "123 Campus Dr", PatronType.STUDENT);

// Borrow book
BorrowRecord record = library.borrowBook("STU001", "978-0134685991");

// Return book
boolean returned = library.returnBook("STU001", "978-0134685991");

// Search books
List<Book> javaBooks = library.searchBooks("title", "Java");

// Reserve book
Reservation reservation = library.reserveBook("STU001", "978-0134685991");
```

### Advanced Features

```java
// Get overdue books
List<BorrowRecord> overdueBooks = library.getOverdueBooks();

// Get patron's borrowing history
List<BorrowRecord> history = library.getPatronBorrowingHistory("STU001");

// Process expired reservations
library.processExpiredReservations();

// Generate inventory report
library.generateInventoryReport();
```

## Technical Requirements

### OOP Concepts Demonstrated
- **Encapsulation**: Private fields with controlled access through getters/setters
- **Inheritance**: Service implementations extend abstract Subject class
- **Polymorphism**: Interface implementations can be used interchangeably
- **Abstraction**: Interfaces define contracts without implementation details

### SOLID Principles Applied
- ✅ Single Responsibility Principle
- ✅ Open/Closed Principle
- ✅ Liskov Substitution Principle
- ✅ Interface Segregation Principle
- ✅ Dependency Inversion Principle

### Design Patterns Implemented
- ✅ Strategy Pattern (Search functionality)
- ✅ Observer Pattern (Notification system)
- ✅ Facade Pattern (Simplified system interface)

### Java Collections Used
- ✅ `ConcurrentHashMap` for thread-safe operations
- ✅ `ArrayList` for ordered collections
- ✅ `List` interface for abstraction
- ✅ Stream API for functional programming

### Logging Framework
- ✅ Custom logging utility with different log levels
- ✅ Timestamp formatting and structured logging

## Future Enhancements

1. **Multi-branch Support**: Extend system to handle multiple library branches
2. **Database Integration**: Add persistence layer with JPA/Hibernate
3. **REST API**: Create RESTful web services
4. **Web Interface**: Develop web-based user interface
5. **Advanced Reporting**: Add comprehensive reporting features
6. **Security**: Implement authentication and authorization
7. **Recommendation System**: AI-based book recommendations

## Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is created for educational purposes and demonstrates Java OOP concepts and design patterns.

---

**Author**: Suvendhu Shekhar Pradhan  
**Date**: September 2025  
**Version**: 1.0