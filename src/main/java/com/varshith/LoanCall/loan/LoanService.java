package com.varshith.LoanCall.loan;

import com.varshith.LoanCall.borrower.Borrower;
import com.varshith.LoanCall.borrower.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BorrowerRepository borrowerRepository;

    public Loan createLoan(
            Long borrowerId,
            CreateLoanRequest request
    ) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        Loan loan = new Loan();

        loan.setBorrower(borrower);
        loan.setEmiAmount(request.emiAmount());
        loan.setDueDay(request.dueDay());
        loan.setStatus(LoanStatus.ACTIVE);

        return loanRepository.save(loan);
    }
}