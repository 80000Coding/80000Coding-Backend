package io.oopy.coding.email.controller;

import io.oopy.coding.email.dto.EmailRequest;
import io.oopy.coding.email.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/email")
@RequiredArgsConstructor
@Slf4j
public class EmailController {

    @Value("${mail.cert.success-redirect-url}")
    private String successRedirectUrl;

    private final EmailService emailService;

    @PostMapping("/send-cert")
    @ResponseBody
    public ResponseEntity sendCertification(@RequestBody EmailRequest.Send emailSend
            //todo - ,@AuthenticationPrincipal UserDetails userDetails)
                            )throws MessagingException {
        String githubId = "12345678";
        emailService.sendCertMail(emailSend.toCertificateEmailSend(githubId));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cert")
    @ResponseBody
    public ResponseEntity deleteCert(
            //todo - ,@AuthenticationPrincipal UserDetails userDetails)
    ){
        String githubId = "12345678";
        emailService.deleteCert(githubId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cert/{githubId}/{token}")
    public void cert(@PathVariable String githubId, @PathVariable String token, HttpServletResponse httpServletResponse) throws Exception{
        emailService.cert(githubId, token);
        httpServletResponse.sendRedirect(successRedirectUrl);
    }
}
