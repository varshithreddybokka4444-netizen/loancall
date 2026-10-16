
package com.varshith.LoanCall.reminder;

import com.varshith.LoanCall.loan.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.YearMonth;
import java.util.Optional;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    Optional<Reminder> findByLoanAndBillingMonth(
            Loan loan,
            YearMonth billingMonth
    );

    @Modifying
    @Query("""
            UPDATE Reminder r
            SET r.status = :newStatus
            WHERE r.id = :reminderId
              AND r.status = :expectedStatus
            """)
    int claimReminder(
            @Param("reminderId") Long reminderId,
            @Param("expectedStatus") ReminderStatus expectedStatus,
            @Param("newStatus") ReminderStatus newStatus
    );
}
