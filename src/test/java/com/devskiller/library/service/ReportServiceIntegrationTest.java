package com.devskiller.library.service;

import com.devskiller.library.model.Book;
import com.devskiller.library.model.BookBorrowing;
import com.devskiller.library.model.User;
import com.devskiller.library.model.UserDueReport;
import com.devskiller.library.repository.BorrowingsRepository;
import com.devskiller.library.repository.MockBorrowingsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static com.devskiller.library.model.BookBorrowing.DEFAULT_LOAN_PERIOD;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

class ReportServiceIntegrationTest {
    private final Long TEST_LOAN_PERIOD;
    private BorrowingsRepository borrowingsRepository;
    private ReportService reportService;

    public ReportServiceIntegrationTest() {
        TEST_LOAN_PERIOD = Long.parseLong(ResourceLoader.getProperty("loan.period.days").orElse(DEFAULT_LOAN_PERIOD));
    }

    @BeforeEach
    void setUp() {
        borrowingsRepository = new MockBorrowingsRepository();
        reportService = new ReportService(borrowingsRepository);
    }

    @Test
    void getUserDueReturnEmptyListIfUserHasNoBorrowings() {
        User user = testUser();

        UserDueReport userDueReport = reportService.getUserDueReport(user);

        assertEquals(user, userDueReport.getUser());
        assertTrue(userDueReport.getBooksDue().isEmpty());
    }

    @Test
    void getUserDueReturnsReport() {
        User user = testUser();
        long overdueDays = 3;
        LocalDate borrowingDateInPast = LocalDate.now().minusDays(TEST_LOAN_PERIOD + overdueDays);
        try (MockedStatic<LocalDate> mockLocalDate = mockStatic(LocalDate.class)) {
            // Scoped mock
            mockLocalDate.when(LocalDate::now).thenReturn(borrowingDateInPast);
            borrowingsRepository.save(user, List.of(new BookBorrowing(testBook())));
        }

        UserDueReport userDueReport = reportService.getUserDueReport(user);

        assertEquals(user, userDueReport.getUser());
        assertEquals(1, userDueReport.getBooksDue().size());
        assertEquals(testBook(), userDueReport.getBooksDue().get(0).getBook());
        assertEquals(ReportService.DAILY_CHARGE.multiply(new BigDecimal(overdueDays)), userDueReport.getBooksDue().get(0).getAmount());
    }

    static Book testBook() {
        return new Book(1111111111111L, "Test1");
    }

    static User testUser() {
        return new User("Test User");
    }

}
