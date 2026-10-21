
package com.varshith.LoanCall.borrower;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateBorrowerRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must not exceed 100 characters")
        String name,

        @NotBlank(message = "Phone number is required")
        @Pattern(
                regexp = "^\\+?[1-9]\\d{9,14}$",
                message = "Phone number must contain 10 to 15 digits, optionally starting with +"
        )
        String phoneNumber

) {
}
