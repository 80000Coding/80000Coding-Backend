package io.oopy.coding.emailcert.controller;

import io.oopy.coding.common.security.CustomUserDetails;
import io.oopy.coding.emailcert.dto.EmailCertRequest;
import io.oopy.coding.emailcert.service.EmailCertService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/email-cert")
@RequiredArgsConstructor
@Slf4j
public class EmailCertController {

    @Value("${mail.cert.success-redirect-url}")
    private String successRedirectUrl;

    private final EmailCertService emailCertService;

    @Operation(summary = "조직 인증메일 발송", description =
            """
                조직 인증메일을 발송한다.
                해당 메일에서 인증하기 버튼을 누르면, 인증이 되는 식으로 구현.
            """
    )
    @PostMapping("/send")
    @ResponseBody
    public ResponseEntity sendCertification(@RequestBody EmailCertRequest.Send emailSend,
            @AuthenticationPrincipal CustomUserDetails securityUser) throws MessagingException {
        emailCertService.sendCertMail(emailSend.toCertificateEmailSend(securityUser.getUserId().toString()));
        return ResponseEntity.noContent().build();
    }

    /**
     * 메일에서 인증하기 버튼을 눌렀을 때, 이메일 인증 진행
     * @param userId
     * @param token
     * @param code
     * @param httpServletResponse
     * @throws Exception
     */
    @GetMapping("/cert/{userId}/{token}/{code}")
    public void cert(@PathVariable String userId, @PathVariable String token, @PathVariable String code, HttpServletResponse httpServletResponse) throws Exception{
        emailCertService.cert(userId, token, code);
        httpServletResponse.sendRedirect(successRedirectUrl);
    }
}
