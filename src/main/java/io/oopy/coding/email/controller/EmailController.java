package io.oopy.coding.email.controller;

import io.oopy.coding.email.controller.dto.EmailRequest;
import io.oopy.coding.email.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-cert")
    public ResponseEntity emailCertificate(@RequestBody EmailRequest.Send emailSend
            //todo - ,@AuthenticationPrincipal UserDetails userDetails)
                            )throws MessagingException {
        String githubId = "12345678";
        emailService.sendCertMail(emailSend.toEmailSend(githubId));
        return ResponseEntity.noContent().build();
    }
}
