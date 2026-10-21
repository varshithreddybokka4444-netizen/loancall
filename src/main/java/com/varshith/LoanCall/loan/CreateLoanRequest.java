
package com.varshith.LoanCall.loan;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CreateLoanRequest(

        @NotNull(message = "EMI amount is required")
        @DecimalMin(
                value = "0.01",
                message = "EMI amount must be greater than zero"
        )
        BigDecimal emiAmount,

        @Min(value = 1, message = "Due day must be between 1 and 31")
        @Max(value = 31, message = "Due day must be between 1 and 31")
        int dueDay

) {
}
