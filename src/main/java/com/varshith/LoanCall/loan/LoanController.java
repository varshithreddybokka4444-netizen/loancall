package com.varshith.LoanCall.loan;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/borrower/{borrowerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Loan createLoan(
            @PathVariable Long borrowerId,
            @RequestBody CreateLoanRequest request
    ) {
        return loanService.createLoan(borrowerId, request);
    }
}