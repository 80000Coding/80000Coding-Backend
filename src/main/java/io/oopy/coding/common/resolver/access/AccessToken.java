package io.oopy.coding.common.resolver.access;

import java.time.LocalDateTime;

public record AccessToken(
        String accessToken,
        Integer githubId,
        LocalDateTime expiryDate,
        boolean isReissued
) {
    public static AccessToken of(String accessToken, Integer githubId, LocalDateTime expiryDate, boolean isReissued) {
        return new AccessToken(accessToken, githubId, expiryDate, isReissued);
    }
}
