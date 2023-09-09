package com.devskiller.library.model;

import java.math.BigDecimal;
import java.util.List;

public class UserDueReport {
    private User user;
    private List<BookDue> booksDue;

    public UserDueReport(User user, List<BookDue> booksDue) {
        this.user = user;
        this.booksDue = booksDue;
    }

    public User getUser() {
        return user;
    }

    public List<BookDue> getBooksDue() {
        return booksDue;
    }

    public static class BookDue {
        private Book book;
        private BigDecimal amount;

        public BookDue(Book book, BigDecimal amount) {
            this.book = book;
            this.amount = amount;
        }

        public Book getBook() {
            return book;
        }

        public BigDecimal getAmount() {
            return amount;
        }
    }
}
