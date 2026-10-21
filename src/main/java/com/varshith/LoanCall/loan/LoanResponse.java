
package com.varshith.LoanCall.loan;

import java.math.BigDecimal;

public record LoanResponse(
        Long id,
        Long borrowerId,
        BigDecimal emiAmount,
        int dueDay,
        LoanStatus status
) {
}
