
package com.varshith.LoanCall.loan;

import com.varshith.LoanCall.borrower.Borrower;
import com.varshith.LoanCall.borrower.BorrowerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final BorrowerRepository borrowerRepository;

    public LoanResponse createLoan(
            Long borrowerId,
            CreateLoanRequest request
    ) {
        Borrower borrower = borrowerRepository.findById(borrowerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Borrower not found"
                ));

        Loan loan = new Loan();
        loan.setBorrower(borrower);
        loan.setEmiAmount(request.emiAmount());
        loan.setDueDay(request.dueDay());
        loan.setStatus(LoanStatus.ACTIVE);

        Loan savedLoan = loanRepository.save(loan);

        return toResponse(savedLoan);
    }

    public List<LoanResponse> getLoansByBorrowerId(Long borrowerId) {
        if (!borrowerRepository.existsById(borrowerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Borrower not found"
            );
        }

        return loanRepository.findByBorrowerId(borrowerId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private LoanResponse toResponse(Loan loan) {
        return new LoanResponse(
                loan.getId(),
                loan.getBorrower().getId(),
                loan.getEmiAmount(),
                loan.getDueDay(),
                loan.getStatus()
        );
    }
}
