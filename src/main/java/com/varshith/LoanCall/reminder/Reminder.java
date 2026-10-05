package com.varshith.LoanCall.reminder;

import com.varshith.LoanCall.loan.Loan;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.YearMonth;

@Entity
@Table(
        name = "reminders",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_loan_billing_month",
                        columnNames = {"loan_id", "billing_month"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Convert(converter = YearMonthConverter.class)
    @Column(name = "billing_month", nullable = false)
    private YearMonth billingMonth;

    @Column(nullable = false)
    private LocalDateTime scheduledAt;

    private LocalDateTime calledAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReminderStatus status = ReminderStatus.SCHEDULED;
}