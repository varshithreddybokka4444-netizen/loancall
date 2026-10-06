package com.varshith.LoanCall.calling;

import com.varshith.LoanCall.reminder.Reminder;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@Slf4j
public class TwilioCallingService implements CallingService {

    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.phone-number}")
    private String twilioPhoneNumber;

    @PostConstruct
    public void initializeTwilio() {

        Twilio.init(accountSid, authToken);

        log.info("Twilio client initialized");
    }

    @Override
    public void callBorrower(Reminder reminder) {

        String borrowerName =
                reminder.getLoan()
                        .getBorrower()
                        .getName();

        String borrowerPhone =
                reminder.getLoan()
                        .getBorrower()
                        .getPhoneNumber();

        String toPhoneNumber =
                normalizeIndianPhoneNumber(borrowerPhone);

        URI twimlUrl = URI.create(
                "https://YOUR-PUBLIC-URL/api/calls/twiml/"
                        + reminder.getId()
        );

        Call call = Call.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(twilioPhoneNumber),
                twimlUrl
        ).create();

        log.info(
                "Twilio call created: callSid={}, borrower={}, phone={}, status={}",
                call.getSid(),
                borrowerName,
                toPhoneNumber,
                call.getStatus()
        );
    }

    private String normalizeIndianPhoneNumber(String phoneNumber) {

        if (phoneNumber.startsWith("+")) {
            return phoneNumber;
        }

        if (phoneNumber.length() == 10) {
            return "+91" + phoneNumber;
        }

        throw new IllegalArgumentException(
                "Invalid phone number: " + phoneNumber
        );
    }
}