package com.varshith.LoanCall.borrower;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Borrower createBorrower(
            @RequestBody CreateBorrowerRequest request
    ) {
        return borrowerService.createBorrower(request);
    }
}