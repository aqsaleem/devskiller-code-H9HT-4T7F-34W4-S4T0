# Library application

## Introduction
You are creating an application allowing a library to manage books count. Your current task is to finish implementation of
missing service methods. Data store is accessed by repository layer to which interfaces are provided in the `repository` package.

## Problem statement
Implement `borrowBook` method in the `BookService` class which should:
- Save information that user borrowed particular book from library
- Decrement available book copies count at library by 1
- Throw `BookNotFoundException` if the book is not available in data store
- Throw `BookNotAvailableException` if the book count is already equal to 0
- Throw `BookAlreadyBorrowedException` if the book is already borrowed by the user

---
Implement `returnBook` method in the `BookService` class which should:
- Save information that user returned particular book to library
- Increment available book copies count at library by 1
- Throw `BookBorrowingNotFoundException` if the user does not have such book borrowed in data store

---
Implement `addBookCopy` method in the `BookService` class which should:
- Save a book copies count of 1 if the book is not present in data store
- Increment copies count by 1 if the book is already present in data store

---
Implement `removeBookCopy` method in the `BookService` class which should:
- Decrement copies count by 1 if the book is present in data store
- Throw `BookNotFoundException` if the book is not present in data store
- Throw `BookNotAvailableException` if the book count is already equal to 0

---
Implement `getAvailableBookCopies` method in the `BookService` class which should:
- Return book count if the book is present in data store
- Throw `BookNotFoundException` if the book is not present in data store

---
Implement `getUserDue` method in the `ReportService` class which should:
- Return a `UserDueReport` class report containing user for which method was called and a list of books which are overdue
according to loan period and fine amount which user should pay on return
- If there are no books overdue on user account, return empty list in the report
  
---
Implement `getProperty` method in `ResourceLoader` class which should:
- Return an `Optional` with value of property according to `name` parameter
- Return an empty `Optional` if such property does not exist
- Properties should be taken from `application.properties` file which is an application resource file
- Properties should support changes during application runtime. If property value changes, new value should be returned
on next method call

## Hints
- Repository interfaces contain Javadocs describing their methods behaviour
- You are not allowed to add/delete/modify methods available in repository interface
- You are not allowed to modify existing tests, but you are free to add new ones
- Remember to keep high code quality
