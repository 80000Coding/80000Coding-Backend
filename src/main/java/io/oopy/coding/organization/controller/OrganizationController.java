package io.oopy.coding.organization.controller;

import io.oopy.coding.common.redis.organizationcert.OrganizationCertification;
import io.oopy.coding.common.redis.organizationcert.OrganizationCertificationService;
import io.oopy.coding.common.redis.organizationcert.dto.OrganizationCertificationDto;
import io.oopy.coding.organization.dto.OrganizationResponse;
import io.oopy.coding.organization.service.OrganizationService;
import io.oopy.coding.organization.service.dto.OrganizationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {

    private final OrganizationService organizationService;
    private final OrganizationCertificationService organizationCertificationService;

    @GetMapping("/users/{id}")
    public ResponseEntity<List<OrganizationResponse.UserOrganization> > getList(@PathVariable Long id) {
        id = 1L;
        List<OrganizationDto.UserOrganization> userOrganizations = organizationService.getUserOrganizations(id);
        List<OrganizationResponse.UserOrganization> response = userOrganizations
                .stream()
                .map(v->OrganizationResponse.UserOrganization.of(v.getCode(), v.getName(), v.getEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/pending/users/{id}")
    public ResponseEntity<List<OrganizationResponse.UserOrganization> > getPendingList(@PathVariable Long id) {
        id = 1L;
        OrganizationCertificationDto.Key key = OrganizationCertificationDto.Key.of(id.toString());
        List<OrganizationCertification> organizationCertifications = organizationCertificationService.getOrganizationCertifications(key);
        List<OrganizationResponse.UserOrganization> response = organizationCertifications
                .stream()
                .map(v -> OrganizationResponse.UserOrganization.of(v.getOrganizationCode(), v.getOrganizationName(), v.getUserEmail()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}
