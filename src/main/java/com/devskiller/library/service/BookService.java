package com.devskiller.library.service;

import static java.util.Objects.isNull;

import com.devskiller.library.exception.BookAlreadyBorrowedException;
import com.devskiller.library.exception.BookBorrowingNotFoundException;
import com.devskiller.library.exception.BookNotAvailableException;
import com.devskiller.library.exception.BookNotFoundException;
import com.devskiller.library.model.Book;
import com.devskiller.library.model.BookBorrowing;
import com.devskiller.library.model.User;
import com.devskiller.library.repository.BooksRepository;
import com.devskiller.library.repository.BorrowingsRepository;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class BookService {
    private final BooksRepository booksRepository;
    private final BorrowingsRepository borrowingsRepository;

    public BookService(BooksRepository booksRepository, BorrowingsRepository borrowingsRepository) {
        this.booksRepository = booksRepository;
        this.borrowingsRepository = borrowingsRepository;
        booksRepository.getDistinctBooksCount();
    }

    public void borrowBook(User user, Book book) {

        if (borrowingsRepository.getByUser(user).
            stream().anyMatch(bookBorrowing -> bookBorrowing.getBook().equals(book))) {
            throw new BookAlreadyBorrowedException();
        }

        Optional<Integer> booksCount = booksRepository.getBookCount(book);
        booksCount.filter(count -> count == 0 ).map(count -> {
            throw new BookNotAvailableException();
        });
        if (booksCount.isEmpty()) {
            throw new BookNotFoundException();
        }
        booksRepository.save(book, booksCount.get()-1);
        List<BookBorrowing> bookBorrowings = borrowingsRepository.getByUser(user);
        bookBorrowings.add(new BookBorrowing(book));
        borrowingsRepository.save(user, bookBorrowings);
    }

    public void returnBook(User user, Book book) {

        if (borrowingsRepository.getByUser(user).
            stream().noneMatch(bookBorrowing -> bookBorrowing.getBook().equals(book))) {
            throw new BookBorrowingNotFoundException();
        }
        Optional<Integer> booksCount = booksRepository.getBookCount(book);
        Integer bookCountIncremented = booksCount.map(count -> count+1)
                .orElse(0);
        booksRepository.save(book, bookCountIncremented);
        List<BookBorrowing> bookBorrowings = borrowingsRepository.getByUser(user);
        bookBorrowings.removeIf(bookBorrowing -> bookBorrowing.getBook().equals(book));
        borrowingsRepository.save(user, bookBorrowings);
    }

    public void addBookCopy(Book book) {
        Optional<Integer> booksCount = booksRepository.getBookCount(book);
        Integer bookCountIncremented = booksCount.map(count -> count+1)
            .orElse(1);
        booksRepository.save(book, bookCountIncremented);
    }

    public void removeBookCopy(Book book) {
        Optional<Integer> booksCount = booksRepository.getBookCount(book);

        booksCount.filter(count -> count == 0 ).map(count -> {
            throw new BookNotAvailableException();
        });
        if (booksCount.isEmpty()) {
            throw new BookNotFoundException();
        }

        Integer bookCountIncremented = booksCount.map(count -> count-1)
            .orElse(0);
        booksRepository.save(book, bookCountIncremented);
    }

    public int getAvailableBookCopies(Book book) {
        Optional<Integer> booksCount  = booksRepository.getBookCount(book);
        if (booksCount.isEmpty()) {
            throw new BookNotFoundException();
        }
        return booksRepository.getBookCount(book).orElse(0);
    }
}
