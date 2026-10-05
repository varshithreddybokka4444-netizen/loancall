package com.varshith.LoanCall.borrower;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    public Borrower createBorrower(CreateBorrowerRequest request) {

        Borrower borrower = new Borrower();

        borrower.setName(request.name());
        borrower.setPhoneNumber(request.phoneNumber());
        borrower.setActive(true);

        return borrowerRepository.save(borrower);
    }
}