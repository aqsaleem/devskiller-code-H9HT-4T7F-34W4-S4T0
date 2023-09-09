package com.devskiller.library.service;

import com.devskiller.library.exception.BookNotAvailableException;
import com.devskiller.library.exception.BookNotFoundException;
import com.devskiller.library.model.Book;
import com.devskiller.library.model.BookBorrowing;
import com.devskiller.library.model.User;
import com.devskiller.library.repository.BooksRepository;
import com.devskiller.library.repository.BorrowingsRepository;
import com.devskiller.library.repository.MockBooksRepository;
import com.devskiller.library.repository.MockBorrowingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.devskiller.library.model.BookBorrowing.DEFAULT_LOAN_PERIOD;
import static org.junit.jupiter.api.Assertions.*;

class BookServiceIntegrationTest {
    private final Long TEST_LOAN_PERIOD;

    private BooksRepository booksRepository;
    private BorrowingsRepository borrowingsRepository;
    private BookService bookService;

    public BookServiceIntegrationTest() {
        TEST_LOAN_PERIOD = Long.parseLong(ResourceLoader.getProperty("loan.period.days").orElse(DEFAULT_LOAN_PERIOD));
    }

    @BeforeEach
    void setUp() {
        booksRepository = new MockBooksRepository();
        borrowingsRepository = new MockBorrowingsRepository();
        bookService = new BookService(booksRepository, borrowingsRepository);
    }

    @Test
    void borrowBookUpdatesUserRecordAndRemovesAvailableCopy() {
        User user = testUser();
        Book book1 = testBook();
        Book book2 = testBook2();
        List<BookBorrowing> userBorrowings = new LinkedList<>();
        userBorrowings.add(new BookBorrowing(book2));
        booksRepository.save(book1, 1);
        borrowingsRepository.save(user, userBorrowings);

        bookService.borrowBook(user, book1);

        userBorrowings = borrowingsRepository.getByUser(user);
        assertEquals(2, userBorrowings.size());
        assertTrue(userBorrowings.stream().map(BookBorrowing::getBook).collect(Collectors.toList()).containsAll(List.of(book1, book2)));
        assertEquals(LocalDate.now(), userBorrowings.get(0).getBorrowingDate());
        assertEquals(LocalDate.now().plusDays(TEST_LOAN_PERIOD), userBorrowings.get(0).getDueDate());
        assertEquals(LocalDate.now(), userBorrowings.get(1).getBorrowingDate());
        assertEquals(LocalDate.now().plusDays(TEST_LOAN_PERIOD), userBorrowings.get(1).getDueDate());
    }

    @Test
    void borrowBookThrowsExceptionIfBookNotFound() {
        User user = testUser();
        Book book = testBook();

        assertThrows(BookNotFoundException.class, () -> bookService.borrowBook(user, book));
        assertEquals(0, borrowingsRepository.getByUser(user).size());
    }

    @Test
    void borrowBookThrowsExceptionIfBookNotAvailable() {
        User user = testUser();
        Book book = testBook();
        booksRepository.save(book, 0);

        assertThrows(BookNotAvailableException.class, () -> bookService.borrowBook(user, book));
        assertEquals(1, booksRepository.getDistinctBooksCount());
        assertEquals(0, booksRepository.getBookCount(book).get());
        assertEquals(0, borrowingsRepository.getByUser(user).size());
    }

    @Test
    void returnBookUpdatesUserRecordAndAddsAvailableCopy() {
        User user = testUser();
        Book book = testBook();
        BookBorrowing bookBorrowing = new BookBorrowing(book);
        List<BookBorrowing> bookBorrowings = new ArrayList<>();
        bookBorrowings.add(bookBorrowing);
        booksRepository.save(book, 1);
        borrowingsRepository.save(user, bookBorrowings);

        bookService.returnBook(user, book);

        assertEquals(0, borrowingsRepository.getByUser(user).size());
        assertEquals(2, booksRepository.getBookCount(book).get());
    }

    @Test
    void addedBookCopyIsAvailable() {
        Book book = testBook();

        bookService.addBookCopy(book);

        assertEquals(1, booksRepository.getDistinctBooksCount());
        assertEquals(1, booksRepository.getBookCount(book).get());
    }

    @Test
    void removeBookCopyRemovesSingleCopy() {
        Book book = testBook();
        booksRepository.save(book, 2);

        bookService.removeBookCopy(book);

        assertEquals(1, booksRepository.getDistinctBooksCount());
        assertEquals(1, booksRepository.getBookCount(book).get());
    }

    @Test
    void removeBookCopyThrowsExceptionWhenBookNotAvailable() {
        Book book = testBook();
        booksRepository.save(book, 0);

        assertThrows(BookNotAvailableException.class, () -> bookService.removeBookCopy(book));
        assertEquals(1, booksRepository.getDistinctBooksCount());
        assertEquals(0, booksRepository.getBookCount(book).get());
    }

    @Test
    void removeBookCopyThrowsExceptionWhenBookNotFound() {
        Book book = testBook();

        assertThrows(BookNotFoundException.class, () -> bookService.removeBookCopy(book));
        assertEquals(0, booksRepository.getDistinctBooksCount());
    }

    @Test
    void getAvailableBookCopies() {
        Book book = testBook();
        booksRepository.save(book, 2);

        assertEquals(2, bookService.getAvailableBookCopies(book));
        assertEquals(1, booksRepository.getDistinctBooksCount());
        assertEquals(2, booksRepository.getBookCount(book).get());
    }

    @Test
    void getAvailableBookCopiesThrowsExceptionWhenBookNotFound() {
        Book book = testBook();

        assertThrows(BookNotFoundException.class, () -> bookService.getAvailableBookCopies(book));
        assertEquals(0, booksRepository.getDistinctBooksCount());
    }

    @Test
    void getAvailableBookCopiesWhenBookNotAvailable() {
        Book book = testBook();
        booksRepository.save(book, 0);

        assertEquals(0, bookService.getAvailableBookCopies(book));
        assertEquals(1, booksRepository.getDistinctBooksCount());
        assertEquals(0, booksRepository.getBookCount(book).get());
    }

    static Book testBook() {
        return new Book(1111111111111L, "Test1");
    }

    static Book testBook2() {
        return new Book(2222222222222L, "Test2");
    }

    static User testUser() {
        return new User("Test User");
    }

}
