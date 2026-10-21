
package com.varshith.LoanCall.loan;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/borrower/{borrowerId}")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanResponse createLoan(
            @PathVariable Long borrowerId,
            @Valid @RequestBody CreateLoanRequest request
    ) {
        return loanService.createLoan(borrowerId, request);
    }

    @GetMapping("/borrower/{borrowerId}")
    public List<LoanResponse> getLoansByBorrowerId(
            @PathVariable Long borrowerId
    ) {
        return loanService.getLoansByBorrowerId(borrowerId);
    }
}
