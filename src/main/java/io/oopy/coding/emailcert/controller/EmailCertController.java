package io.oopy.coding.emailcert.controller;

import io.oopy.coding.emailcert.dto.EmailCertRequest;
import io.oopy.coding.emailcert.service.EmailCertService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/email-cert")
@RequiredArgsConstructor
@Slf4j
public class EmailCertController {

    @Value("${mail.cert.success-redirect-url}")
    private String successRedirectUrl;

    private final EmailCertService emailService;

    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity sendCertification(@RequestBody EmailCertRequest.Send emailSend
            //todo - ,@AuthenticationPrincipal UserDetails userDetails)
                            )throws MessagingException {
        String userId = "1";
        emailService.sendCertMail(emailSend.toCertificateEmailSend(userId));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cert")
    @ResponseBody
    public ResponseEntity deleteCert(
            @RequestBody EmailCertRequest.Remove cert
            //todo - ,@AuthenticationPrincipal UserDetails userDetails)
    ){
        String userId = "1";
        emailService.deleteCert(userId, cert.getCode());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cert/{userId}/{token}/{code}")
    public void cert(@PathVariable String userId, @PathVariable String token, @PathVariable String code, HttpServletResponse httpServletResponse) throws Exception{
        emailService.cert(userId, token, code);
        httpServletResponse.sendRedirect(successRedirectUrl);
    }
}
