
package com.varshith.LoanCall.borrower;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrowers")
@RequiredArgsConstructor
public class BorrowerController {

    private final BorrowerService borrowerService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BorrowerResponse createBorrower(
            @Valid @RequestBody CreateBorrowerRequest request
    ) {
        return borrowerService.createBorrowerResponse(request);
    }

    @GetMapping
    public Page<BorrowerResponse> getAllBorrowers(Pageable pageable) {
        return borrowerService.getAllBorrowers(pageable);
    }

    @GetMapping("/{id}")
    public BorrowerResponse getBorrowerById(@PathVariable Long id) {
        return borrowerService.getBorrowerById(id);
    }
}
