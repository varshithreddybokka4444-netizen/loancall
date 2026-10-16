package com.varshith.LoanCall;

import com.varshith.LoanCall.loan.Loan;
import com.varshith.LoanCall.reminder.Reminder;
import com.varshith.LoanCall.reminder.ReminderRepository;
import com.varshith.LoanCall.reminder.ReminderService;
import com.varshith.LoanCall.reminder.ReminderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanCallApplicationTests {

	@Mock
	private ReminderRepository reminderRepository;

	@InjectMocks
	private ReminderService reminderService;

	@Test
	void createReminderShouldSaveScheduledReminderWhenNoneExists() {
		Loan loan = new Loan();
		YearMonth billingMonth = YearMonth.of(2026, 11);
		LocalDateTime scheduledAt =
				LocalDateTime.of(2026, 10, 30, 9, 0);

		when(reminderRepository.findByLoanAndBillingMonth(
				loan, billingMonth
		)).thenReturn(Optional.empty());

		when(reminderRepository.save(any(Reminder.class)))
				.thenAnswer(invocation -> invocation.getArgument(0));

		Reminder result = reminderService.createReminder(
				loan, billingMonth, scheduledAt
		);

		assertEquals(loan, result.getLoan());
		assertEquals(billingMonth, result.getBillingMonth());
		assertEquals(scheduledAt, result.getScheduledAt());
		assertEquals(ReminderStatus.SCHEDULED, result.getStatus());

		verify(reminderRepository).save(any(Reminder.class));
	}

	@Test
	void createReminderShouldReuseExistingReminder() {
		Loan loan = new Loan();
		YearMonth billingMonth = YearMonth.of(2026, 11);

		Reminder existing = new Reminder();
		existing.setLoan(loan);
		existing.setBillingMonth(billingMonth);
		existing.setStatus(ReminderStatus.CALLED);

		when(reminderRepository.findByLoanAndBillingMonth(
				loan, billingMonth
		)).thenReturn(Optional.of(existing));

		Reminder result = reminderService.createReminder(
				loan, billingMonth, LocalDateTime.now()
		);

		assertSame(existing, result);
		assertEquals(ReminderStatus.CALLED, result.getStatus());

		verify(reminderRepository, never()).save(any(Reminder.class));
	}
}