package com.varshith.LoanCall.reminder;

import com.varshith.LoanCall.loan.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public Reminder createReminder(
            Loan loan,
            YearMonth billingMonth,
            LocalDateTime scheduledAt
    ) {
        return reminderRepository
                .findByLoanAndBillingMonth(loan, billingMonth)
                .orElseGet(() -> {
                    Reminder reminder = new Reminder();

                    reminder.setLoan(loan);
                    reminder.setBillingMonth(billingMonth);
                    reminder.setScheduledAt(scheduledAt);
                    reminder.setStatus(ReminderStatus.SCHEDULED);

                    return reminderRepository.save(reminder);
                });
    }
}