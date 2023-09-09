package com.devskiller.library.repository;

import com.devskiller.library.model.BookBorrowing;
import com.devskiller.library.model.User;

import java.util.List;

/**
 * Interface responsible for management of users borrowings
 */
public interface BorrowingsRepository {

    /**
     * Return list of books borrowed by user
     * @param user User for which borrowings should be returned
     * @return List of currently borrowed books or empty list if user has no books borrowed
     */
    List<BookBorrowing> getByUser(User user);

    /**
     * Save information about books currently borrowed in data store.
     * @param user User for which books borrowings is saved
     * @param booksBorrowings List of books currently borrowed by user
     */
    void save(User user, List<BookBorrowing> booksBorrowings);

}
