package com.varshith.LoanCall.reminder;

import com.varshith.LoanCall.loan.Loan;
import com.varshith.LoanCall.loan.LoanRepository;
import com.varshith.LoanCall.loan.LoanStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final LoanRepository loanRepository;
    private final ReminderService reminderService;

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void processDueReminders() {

        LocalDate today = LocalDate.now();

        List<Loan> activeLoans =
                loanRepository.findByStatus(LoanStatus.ACTIVE);

        for (Loan loan : activeLoans) {

            LocalDate dueDate =
                    calculateDueDate(today, loan.getDueDay());

            LocalDate reminderDate =
                    dueDate.minusDays(2);

            if (today.equals(reminderDate)) {

                YearMonth billingMonth =
                        YearMonth.from(dueDate);

                Reminder reminder =
                        reminderService.createReminder(
                                loan,
                                billingMonth,
                                LocalDateTime.now()
                        );

                log.info(
                        "Reminder ready: loanId={}, borrower={}, billingMonth={}, scheduledAt={}, status={}",
                        loan.getId(),
                        loan.getBorrower().getName(),
                        reminder.getBillingMonth(),
                        reminder.getScheduledAt(),
                        reminder.getStatus()
                );
            }
        }
    }

    private LocalDate calculateDueDate(
            LocalDate today,
            int dueDay
    ) {
        YearMonth currentMonth =
                YearMonth.from(today);

        int validDueDay =
                Math.min(dueDay, currentMonth.lengthOfMonth());

        return currentMonth.atDay(validDueDay);
    }
}