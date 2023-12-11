package io.oopy.coding.emailcert.service;

import io.oopy.coding.common.email.service.EmailService;
import io.oopy.coding.common.redis.organizationcert.OrganizationCertification;
import io.oopy.coding.common.redis.organizationcert.OrganizationCertificationService;
import io.oopy.coding.common.redis.organizationcert.dto.OrganizationCertificationDto;
import io.oopy.coding.common.response.code.ErrorCode;
import io.oopy.coding.common.response.exception.GlobalErrorException;
import io.oopy.coding.emailcert.service.dto.EmailCertificateDto;
import io.oopy.coding.organization.service.OrganizationService;
import io.oopy.coding.organization.service.dto.OrganizationDto;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

import static io.oopy.coding.common.constant.EmailConstant.CERT_CONTENT;

@Service
@RequiredArgsConstructor
public class EmailCertService {

    @Value("${mail.cert.domain}")
    private String emailCertDomain;
    private final OrganizationCertificationService organizationCertificationService;
    private final EmailService emailService;
    private final OrganizationService organizationService;


    @Transactional
    public void sendCertMail(EmailCertificateDto.Send emailSend) throws MessagingException {
        checkSendValid(emailSend);
        String token = UUID.randomUUID().toString();
        emailSend.setContent(String.format(CERT_CONTENT,
                emailCertDomain,
                emailSend.getUserId(),
                token,
                emailSend.getOrganizationCode()
        ));
        OrganizationDto.Organization organizationByCode = organizationService.getOrganizationByCode(emailSend.getOrganizationCode());
        organizationCertificationService.register(
                new OrganizationCertificationDto.Register(
                        emailSend.getUserId(), token, emailSend.getOrganizationCode(),
                        organizationByCode.getName(),emailSend.getTargetEmail()
        ));
        emailService.sendMail(emailSend);
    }

    @Transactional
    public void cert(String userId, String token, String code) {
        Optional<OrganizationCertification> emailCertification = organizationCertificationService.getOrganizationCertification(
                OrganizationCertificationDto.Cert.of(userId, token, code));
        if (emailCertification.isEmpty()) {
            throw new GlobalErrorException(ErrorCode.NOT_FOUND_ORGANIZATION_CERT);
        }
        if (!token.equals(emailCertification.get().getCertToken())) {
            throw new GlobalErrorException(ErrorCode.INVALID_ORGANIZATION_CERT_TOKEN);
        }
        organizationService.setUserOrganization(
                OrganizationDto.Cert.of(code, Long.parseLong(userId), emailCertification.get().getUserEmail()));
        organizationCertificationService.delete(
                OrganizationCertificationDto.Delete.of(userId, code)
        );
    }

    private void checkSendValid(EmailCertificateDto.Send emailSend) {
        Optional<OrganizationCertification> emailCertificationBySet = organizationCertificationService.getOrganizationCertification(
                OrganizationCertificationDto.Get.of(emailSend.getUserId(), emailSend.getOrganizationCode()));
        if (emailCertificationBySet.isPresent()) {
            throw new GlobalErrorException(ErrorCode.ALREADY_ORGANIZATION_CERT);
        }
    }
}
