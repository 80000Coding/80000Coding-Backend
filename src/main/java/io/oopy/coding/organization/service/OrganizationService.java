package io.oopy.coding.organization.service;

import io.oopy.coding.common.response.code.ErrorCode;
import io.oopy.coding.common.response.exception.GlobalErrorException;
import io.oopy.coding.domain.organization.entity.Organization;
import io.oopy.coding.domain.organization.entity.UserOrganization;
import io.oopy.coding.domain.organization.repository.OrganizationRepository;
import io.oopy.coding.domain.organization.repository.UserOrganizationRepository;
import io.oopy.coding.domain.user.entity.User;
import io.oopy.coding.organization.service.dto.OrganizationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrganizationService {

    private final UserOrganizationRepository userOrganizationRepository;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public OrganizationDto.Organization getOrganizationByCode(String code) {
        Organization organization = organizationRepository.findByCode(code).orElseThrow(() -> new GlobalErrorException(ErrorCode.NOT_FOUND_ORGANIZATION));
        return OrganizationDto.Organization.of(
                organization.getName(), organization.getCode(), organization.getDescription());
    }

    @Transactional
    public List<OrganizationDto.UserOrganization> getUserOrganizations(Long userId) {
        List<UserOrganization> userOrganizations = userOrganizationRepository.findByUser(User.builder()
                .id(userId).build());
        if (ObjectUtils.isEmpty(userOrganizations)) {
            return new ArrayList<>();
        }
        List<OrganizationDto.UserOrganization> organizations = userOrganizations.stream()
                .map(v -> OrganizationDto.UserOrganization.of(v.getOrganization().getName(),
                        v.getOrganization().getCode(),
                        v.getOrganization().getDescription(),
                        v.getEmail()
                ))
                .collect(toList());
        return organizations;
    }

    @Transactional
    public void setUserOrganization(OrganizationDto.Cert cert) {
        Organization organization = organizationRepository.findByCode(cert.getOrganizationCode()).orElseThrow(()-> new GlobalErrorException(ErrorCode.NOT_FOUND_ORGANIZATION));
        UserOrganization userOrganization = UserOrganization.of(organization,
                User.builder()
                    .id(cert.getUserId()).build(), cert.getUserEmail());
        userOrganizationRepository.save(userOrganization);
    }

    public boolean isUserAlreadyRegistered(OrganizationDto.Cert cert) {
        List<OrganizationDto.UserOrganization> userOrganizations =
                getUserOrganizations(cert.getUserId());
        Optional<OrganizationDto.UserOrganization> organization = userOrganizations.stream()
                .filter(v -> v.getCode().equals(cert.getOrganizationCode()))
                .findFirst();
        return organization.isEmpty() ? false : true;
    }
}
