package com.devskiller.library.repository;

import com.devskiller.library.model.Book;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MockBooksRepository implements BooksRepository {
    private final Map<Book, Integer> booksCountMap = new HashMap<>();

    @Override
    public Integer getDistinctBooksCount() {
        return booksCountMap.size();
    }

    @Override
    public Optional<Integer> getBookCount(Book book) {
        return Optional.ofNullable(booksCountMap.get(book));
    }

    @Override
    public void save(Book book, int count) {
        booksCountMap.put(book, count);
    }

}
