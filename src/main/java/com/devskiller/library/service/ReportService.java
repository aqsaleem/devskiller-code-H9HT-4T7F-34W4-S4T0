package com.devskiller.library.service;

import static java.time.temporal.ChronoUnit.DAYS;

import com.devskiller.library.model.BookBorrowing;
import com.devskiller.library.model.User;
import com.devskiller.library.model.UserDueReport;
import com.devskiller.library.model.UserDueReport.BookDue;
import com.devskiller.library.repository.BorrowingsRepository;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;

public class ReportService {
    public static final BigDecimal DAILY_CHARGE = new BigDecimal("0.5");

    private final BorrowingsRepository borrowingsRepository;

    public ReportService(BorrowingsRepository borrowingsRepository) {
        this.borrowingsRepository = borrowingsRepository;
    }

    public UserDueReport getUserDueReport(User user) {
        List<BookBorrowing> bookBorrowings = borrowingsRepository.getByUser(user);
        LocalDate currentDate = LocalDate.now();
       List<BookDue> bookDues= bookBorrowings.stream()
           .filter(bookBorrowing -> Duration.ofDays(DAYS.between(bookBorrowing.getDueDate(), currentDate)).toDays() >= 0)
            .map(bookBorrowing -> {
                long days = Duration.ofDays(DAYS.between(bookBorrowing.getDueDate(), currentDate)).toDays();
                return new BookDue(bookBorrowing.getBook(), BigDecimal.valueOf(days).multiply(DAILY_CHARGE));
            }).toList();
        return new UserDueReport(user, bookDues);
    }

}
