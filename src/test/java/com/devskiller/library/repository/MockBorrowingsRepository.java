package com.devskiller.library.repository;

import com.devskiller.library.model.BookBorrowing;
import com.devskiller.library.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockBorrowingsRepository implements BorrowingsRepository {
    private final Map<User, List<BookBorrowing>> usersBorrowingsMap = new HashMap<>();

    @Override
    public List<BookBorrowing> getByUser(User user) {
        return new ArrayList<>(usersBorrowingsMap.getOrDefault(user, new ArrayList<>()));
    }

    @Override
    public void save(User user, List<BookBorrowing> booksBorrowings) {
        usersBorrowingsMap.put(user, booksBorrowings);
    }

}
