package com.devskiller.library.model;

import com.devskiller.library.service.ResourceLoader;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class BookBorrowing {
    public static final String DEFAULT_LOAN_PERIOD = null;

    private UUID id;
    private Book book;
    private LocalDate borrowingDate;
    private LocalDate dueDate;

    public BookBorrowing(Book book) {
        this.id = UUID.randomUUID();
        this.book = book;
        this.borrowingDate = LocalDate.now();
        String loanDays = ResourceLoader.getProperty("loan.period.days").orElse(DEFAULT_LOAN_PERIOD);
        this.dueDate = borrowingDate.plusDays(Long.parseLong(loanDays));
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public LocalDate getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(LocalDate borrowingDate) {
        this.borrowingDate = borrowingDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookBorrowing that = (BookBorrowing) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
