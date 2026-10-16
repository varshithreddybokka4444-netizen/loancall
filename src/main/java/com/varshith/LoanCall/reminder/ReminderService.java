
package com.varshith.LoanCall.reminder;

import com.varshith.LoanCall.loan.Loan;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.transaction.annotation.Propagation;
import java.time.LocalDateTime;
import java.time.YearMonth;

@Service
@RequiredArgsConstructor
public class ReminderService {

    private final ReminderRepository reminderRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
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

    @Transactional(
            propagation = org.springframework.transaction.annotation.Propagation.REQUIRES_NEW
    )
    public boolean claimReminder(Long reminderId) {
        return reminderRepository.claimReminder(
                reminderId,
                ReminderStatus.SCHEDULED,
                ReminderStatus.CALLING
        ) == 1;
    }

    @Transactional
    public Reminder markCalled(Reminder reminder) {
        reminder.setStatus(ReminderStatus.CALLED);
        reminder.setCalledAt(LocalDateTime.now());

        return reminderRepository.save(reminder);
    }

    @Transactional
    public Reminder markFailed(Reminder reminder) {
        reminder.setStatus(ReminderStatus.FAILED);

        return reminderRepository.save(reminder);
    }
}
