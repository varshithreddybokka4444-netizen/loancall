package com.varshith.LoanCall.borrower;

public record BorrowerResponse(
        Long id,
        String name,
        String phoneNumber,
        Boolean active
) {
}