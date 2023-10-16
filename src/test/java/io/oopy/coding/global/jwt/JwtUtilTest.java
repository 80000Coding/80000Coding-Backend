package io.oopy.coding.global.jwt;

import io.oopy.coding.domain.user.entity.RoleType;
import io.oopy.coding.common.util.jwt.entity.JwtUserInfo;
import io.oopy.coding.common.util.jwt.JwtUtil;
import io.oopy.coding.common.util.jwt.JwtUtilImpl;
import io.oopy.coding.common.util.redis.refresh.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtUtilTest {
    private JwtUtil jwtUtil;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private JwtUserInfo dto;
    private static final String jwtSecretKey = "exampleSecretKeyForSpringBootProjectAtSubRepository";

    @BeforeEach
    public void setUp() {
        jwtUtil = new JwtUtilImpl(jwtSecretKey, 1);
        dto = createDto();
    }

    @Test
    public void testGenerateAccessToken() {
        // when
        String accessToken = jwtUtil.generateAccessToken(dto);

        // then
        System.out.println("accessToken : " + accessToken);
        assertNotNull(accessToken);
    }

    @Test
    public void testGenerateRefreshToken() {
        // when
        String refreshToken = jwtUtil.generateRefreshToken(dto);

        // then
        System.out.println("refreshToken : " + refreshToken);
        assertNotNull(refreshToken);
    }

    @Test
    public void testIsTokenExpired() {
        // given
        String header = "Bearer " + ExpiredTokenGenerator.generateExpiredToken(dto, jwtSecretKey);

        // when
        assertThrows(RuntimeException.class, () -> jwtUtil.resolveToken(header));
    }

    @Test
    public void testAccessTokenRefresh() {
        // given
        ReflectionTestUtils.setField(jwtUtil, "accessTokenExpirationTime", -10);
        ReflectionTestUtils.setField(jwtUtil, "refreshTokenExpirationTime", 10);
        String header = "Bearer " + jwtUtil.generateAccessToken(dto);
        String refreshToken = jwtUtil.generateRefreshToken(dto);
        
        // when
        System.out.println("=============== testAccessTokenRefresh ===============");
        String token = jwtUtil.resolveToken(header);
        System.out.println("token : " + token);
        System.out.println("=============== testAccessTokenRefresh ===============");

        // then
        assertNotNull(token);
    }

    @Test
    public void RefreshTokenExpired() {

    }

    @Test
    public void testReceiveUserIdFromToken() {
        // given
        String token = jwtUtil.generateAccessToken(dto);

        // when
        Long userId = jwtUtil.getUserIdFromToken(token);

        // then
        System.out.println("userId : " + userId);
        assertEquals(dto.getId(), userId);
    }

    @Test
    public void testReceiveGithubIdFromToken() {
        // given
        String token = jwtUtil.generateAccessToken(dto);

        // when
        Integer githubId = ReflectionTestUtils.invokeMethod(jwtUtil, "getGithubIdFromToken", token);

        // then
        System.out.println("githubId : " + githubId);
        assertEquals(dto.getGithubId(), githubId);
    }

    private JwtUserInfo createDto() {
        return JwtUserInfo.builder()
                .id(1L)
                .githubId(1)
                .role(RoleType.USER)
                .build();
    }
}