
package com.varshith.LoanCall.loan;

import com.varshith.LoanCall.borrower.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findByStatus(LoanStatus status);

    List<Loan> findByBorrowerId(Long borrowerId);
}