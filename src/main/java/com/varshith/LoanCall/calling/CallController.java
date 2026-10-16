
package com.varshith.LoanCall.calling;

import com.varshith.LoanCall.reminder.Reminder;
import com.varshith.LoanCall.reminder.ReminderRepository;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/calls")
@RequiredArgsConstructor
public class CallController {

    private final ReminderRepository reminderRepository;
    private final CallingService callingService;

    @PostMapping("/test/{reminderId}")
    public String testCall(@PathVariable Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() ->
                        new RuntimeException("Reminder not found"));

        callingService.callBorrower(reminder);
        return "Call initiated successfully";
    }

    @RequestMapping(
            value = "/twiml/{reminderId}",
            method = {RequestMethod.GET, RequestMethod.POST},
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public String getCallTwiml(@PathVariable Long reminderId) {
        Reminder reminder = reminderRepository.findById(reminderId)
                .orElseThrow(() ->
                        new RuntimeException("Reminder not found"));

        String borrowerName = reminder.getLoan()
                .getBorrower().getName();

        BigDecimal emiAmount = reminder.getLoan().getEmiAmount();

        String message = String.format(
                "Hello %s. This is a reminder regarding your upcoming loan EMI payment. "
                        + "Your EMI amount is %s rupees. Thank you.",
                borrowerName,
                emiAmount
        );

        Say say = new Say.Builder(message)
                .voice(Say.Voice.ALICE)
                .language(Say.Language.EN_IN)
                .build();

        return new VoiceResponse.Builder()
                .say(say)
                .build()
                .toXml();
    }
}
