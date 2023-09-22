package io.oopy.coding.organization.controller;

import io.oopy.coding.common.redis.organizationcert.OrganizationCertification;
import io.oopy.coding.common.redis.organizationcert.OrganizationCertificationService;
import io.oopy.coding.common.redis.organizationcert.dto.OrganizationCertificationDto;
import io.oopy.coding.common.security.CustomUserDetails;
import io.oopy.coding.emailcert.dto.EmailCertRequest;
import io.oopy.coding.organization.dto.OrganizationResponse;
import io.oopy.coding.organization.service.OrganizationService;
import io.oopy.coding.organization.service.dto.OrganizationDto;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationCertificationService organizationCertificationService;

    @Operation(summary = "인증된 조직들 내역 조회", description =
            """
                인증된 조직들 내역을 조회한다.
            """
    )
    @GetMapping("/users")
    public ResponseEntity<List<OrganizationResponse.UserOrganization> > getList(@AuthenticationPrincipal CustomUserDetails securityUser) {
        List<OrganizationDto.UserOrganization> userOrganizations = organizationService.getUserOrganizations(securityUser.getUserId());
        List<OrganizationResponse.UserOrganization> response = userOrganizations
                .stream()
                .map(v->OrganizationResponse.UserOrganization.of(v.getCode(), v.getName(), v.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "인증 진행중인 조직 내역 조회", description =
            """
                인증된 조직들 내역을 조회한다.
            """
    )
    @GetMapping("/pending/users")
    public ResponseEntity<List<OrganizationResponse.UserOrganization> > getPendingList(@AuthenticationPrincipal CustomUserDetails securityUser) {
        OrganizationCertificationDto.Key key = OrganizationCertificationDto.Key.of(securityUser.getUserId().toString());
        List<OrganizationCertification> organizationCertifications = organizationCertificationService.getOrganizationCertifications(key);
        List<OrganizationResponse.UserOrganization> response = organizationCertifications
                .stream()
                .map(v -> OrganizationResponse.UserOrganization.of(v.getOrganizationCode(), v.getOrganizationName(), v.getUserEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "조직 인증 진행 취소", description =
            """
                조직 인증 진행을 취소한다.
            """
    )
    @DeleteMapping("/cert")
    public ResponseEntity deleteCert(
            @RequestBody EmailCertRequest.Remove cert,
            @AuthenticationPrincipal CustomUserDetails securityUser){
        organizationCertificationService.delete(
                OrganizationCertificationDto.Delete.of(
                        securityUser.getUserId().toString(),
                        cert.getCode()
                ));
        return ResponseEntity.noContent().build();
    }
}
