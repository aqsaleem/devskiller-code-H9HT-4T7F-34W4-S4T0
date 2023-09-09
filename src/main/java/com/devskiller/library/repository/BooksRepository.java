package com.devskiller.library.repository;

import com.devskiller.library.model.Book;

import java.util.Optional;

/**
 * Interface responsible for management of books availability
 */
public interface BooksRepository {

    /**
     * Return count of distinct books in the library. Every title is counted once even if currently available copies
     * are equal to 0.
     * @return Count of distinct books
     */
    Integer getDistinctBooksCount();

    /**
     * Return count of book copies currently available in library.
     * @param book Book for which count should be provided
     * @return Count of available book copies or empty optional if book is unknown
     */
    Optional<Integer> getBookCount(Book book);

    /**
     * Save information about currently available book copies count in data store.
     * @param book Book for which count should be saved
     * @param count Currently available copies
     */
    void save(Book book, int count);

}
