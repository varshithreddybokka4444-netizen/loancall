
package com.varshith.LoanCall.reminder;

import com.varshith.LoanCall.calling.CallingService;
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
    private final CallingService callingService;

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void processDueReminders() {
        LocalDate today = LocalDate.now();
        YearMonth currentMonth = YearMonth.from(today);

        List<Loan> activeLoans =
                loanRepository.findByStatus(LoanStatus.ACTIVE);

        for (Loan loan : activeLoans) {
            LocalDate currentMonthDueDate =
                    calculateDueDate(currentMonth, loan.getDueDay());

            LocalDate nextMonthDueDate =
                    calculateDueDate(
                            currentMonth.plusMonths(1),
                            loan.getDueDay()
                    );

            LocalDate dueDate = null;

            if (today.equals(currentMonthDueDate.minusDays(2))) {
                dueDate = currentMonthDueDate;
            } else if (today.equals(nextMonthDueDate.minusDays(2))) {
                dueDate = nextMonthDueDate;
            }

            if (dueDate == null) {
                continue;
            }

            YearMonth billingMonth = YearMonth.from(dueDate);

            Reminder reminder = reminderService.createReminder(
                    loan,
                    billingMonth,
                    LocalDateTime.now()
            );

            if (!reminderService.claimReminder(reminder.getId())) {
                log.info(
                        "Reminder already claimed or processed: reminderId={}, status={}",
                        reminder.getId(),
                        reminder.getStatus()
                );
                continue;
            }

            try {
                callingService.callBorrower(reminder);
                reminderService.markCalled(reminder);

                log.info(
                        "Borrower call initiated: loanId={}, borrower={}",
                        loan.getId(),
                        loan.getBorrower().getName()
                );

            } catch (Exception exception) {
                reminderService.markFailed(reminder);

                log.error(
                        "Borrower call failed: loanId={}, borrower={}",
                        loan.getId(),
                        loan.getBorrower().getName(),
                        exception
                );
            }
        }
    }

    private LocalDate calculateDueDate(
            YearMonth month,
            int dueDay
    ) {
        int validDueDay = Math.min(
                dueDay,
                month.lengthOfMonth()
        );

        return month.atDay(validDueDay);
    }
}
