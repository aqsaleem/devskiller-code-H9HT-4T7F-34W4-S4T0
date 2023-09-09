package com.devskiller.library.model;

import java.util.Objects;

public class Book {
    private Long isbn;
    private String title;

    public Book(Long isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }

    public Long getIsbn() {
        return isbn;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
