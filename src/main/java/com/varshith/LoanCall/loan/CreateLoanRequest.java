package com.varshith.LoanCall.loan;

import java.math.BigDecimal;

public record CreateLoanRequest(
        BigDecimal emiAmount,
        int dueDay
) {
}