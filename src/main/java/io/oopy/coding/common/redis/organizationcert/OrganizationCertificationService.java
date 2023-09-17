package io.oopy.coding.common.redis.organizationcert;

import io.oopy.coding.common.redis.organizationcert.dto.OrganizationCertificationDto;
import io.oopy.coding.common.redis.organizationcert.dto.OrganizationCertKey;
import io.oopy.coding.common.redis.organizationcert.dto.OrganizationKey;
import io.oopy.coding.organization.service.OrganizationService;
import io.oopy.coding.organization.service.dto.OrganizationDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class OrganizationCertificationService {

    private final ListOperations<String, OrganizationCertification> listOperations;
    private final RedisTemplate<String, OrganizationCertification> redisTemplate;
    private final OrganizationService organizationService;
    private static final long EXPIRED_TIME = 600;

    public OrganizationCertificationService(RedisTemplate<String, OrganizationCertification> redisTemplate,
                                            OrganizationService organizationService) {
        this.listOperations = redisTemplate.opsForList();
        this.redisTemplate = redisTemplate;
        this.organizationService = organizationService;
    }

    @Transactional(readOnly = true)
    public List<OrganizationCertification> getOrganizationCertifications(OrganizationKey organizationCertKey) {
        List<OrganizationCertification> organizationCertifications = listOperations.range(organizationCertKey.getKey(), 0, -1);
        if (ObjectUtils.isEmpty(organizationCertifications))
            return new ArrayList<>();
        List<OrganizationCertification> filteredOrganizationCertifications = organizationCertifications.stream()
                .filter(v -> v.isExpired())
                .collect(Collectors.toList());
        return ObjectUtils.isEmpty(filteredOrganizationCertifications) ?
                new ArrayList<>() :
                filteredOrganizationCertifications;
    }

    @Transactional
    public void register(OrganizationCertificationDto.Register register) {
        if(organizationService.isUserAlreadyRegistered(
                OrganizationDto.Cert.of(register.getOrganizationCode(),
                        Long.parseLong(register.userId()),
                        register.userEmail()
                ))) {
                throw new RuntimeException("이미 인증된 조직입니다.");
        }
        Optional<OrganizationCertification> checkOrganizationCertification = getOrganizationCertification(register);
        if (checkOrganizationCertification.isPresent()) {
            throw new RuntimeException("인증 진행중입니다.");
        }
        OrganizationCertification organizationCertification = OrganizationCertification.of(register.token(),
                register.userId(),
                register.userEmail(),
                register.code(),
                register.organizationName(),
                EXPIRED_TIME
        );
        listOperations.rightPush(register.getKey(), organizationCertification);
        ZoneId zoneId = ZoneId.systemDefault();
        redisTemplate.expireAt(register.getKey(), organizationCertification.getExpiredAt().atZone(zoneId)
                .toInstant());
    }

    @Transactional
    public void delete(OrganizationCertKey organizationCertKey) {
        Optional<OrganizationCertification> organizationCertification = getOrganizationCertification(organizationCertKey);
        if (organizationCertification.isEmpty()) {
            throw new RuntimeException("인증 내역 없음.");
        }
        listOperations.remove(organizationCertKey.getKey(), 0, organizationCertification.get());
    }

    public Optional<OrganizationCertification> getOrganizationCertification(OrganizationCertKey cert) {
        List<OrganizationCertification> organizationCertificationList = getOrganizationCertifications(cert);
        if (ObjectUtils.isEmpty(organizationCertificationList)) {
            return Optional.empty();
        }
        Optional<OrganizationCertification> organizationCertification = organizationCertificationList.stream()
                .filter(v -> v.getOrganizationCode() != null &&
                        v.getOrganizationCode().equals(cert.getOrganizationCode()))
                .findFirst();
        return organizationCertification;
    }
}