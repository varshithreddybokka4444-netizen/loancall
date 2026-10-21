
package com.varshith.LoanCall.borrower;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class BorrowerService {

    private final BorrowerRepository borrowerRepository;

    public Borrower createBorrower(CreateBorrowerRequest request) {
        if (borrowerRepository.existsByPhoneNumber(request.phoneNumber())) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "A borrower with this phone number already exists"
            );
        }

        Borrower borrower = new Borrower();
        borrower.setName(request.name());
        borrower.setPhoneNumber(request.phoneNumber());
        borrower.setActive(true);

        return borrowerRepository.save(borrower);
    }

    public Page<BorrowerResponse> getAllBorrowers(Pageable pageable) {
        return borrowerRepository.findAll(pageable)
                .map(this::toResponse);
    }

    public BorrowerResponse getBorrowerById(Long id) {
        Borrower borrower = borrowerRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Borrower not found"
                ));

        return toResponse(borrower);
    }

    public BorrowerResponse createBorrowerResponse(
            CreateBorrowerRequest request
    ) {
        return toResponse(createBorrower(request));
    }

    private BorrowerResponse toResponse(Borrower borrower) {
        return new BorrowerResponse(
                borrower.getId(),
                borrower.getName(),
                borrower.getPhoneNumber(),
                borrower.getActive()
        );
    }
}
