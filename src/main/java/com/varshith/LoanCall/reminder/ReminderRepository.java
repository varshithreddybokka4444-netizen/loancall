package com.varshith.LoanCall.reminder;

import com.varshith.LoanCall.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    Optional<Reminder> findByLoanAndBillingMonth(
            Loan loan,
            YearMonth billingMonth
    );
}