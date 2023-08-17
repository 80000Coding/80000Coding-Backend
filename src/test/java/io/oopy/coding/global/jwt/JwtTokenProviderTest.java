package io.oopy.coding.global.jwt;

import io.oopy.coding.domain.user.domain.RoleType;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import io.oopy.coding.global.redis.refresh.RefreshTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    private UserAuthenticateDto dto;
    private static final String jwtSecretKey = "exampleSecretKeyForSpringBootProjectAtSubRepository";

    @BeforeEach
    public void setUp() {
        jwtTokenProvider = new JwtTokenProviderImpl(jwtSecretKey, 1, 1, refreshTokenRepository);
        dto = createDto();
    }

    @Test
    public void testGenerateAccessToken() {
        // when
        String accessToken = jwtTokenProvider.generateAccessToken(dto);

        // then
        System.out.println("accessToken : " + accessToken);
        assertNotNull(accessToken);
    }

    @Test
    public void testGenerateRefreshToken() {
        // when
        String refreshToken = jwtTokenProvider.generateRefreshToken(dto);

        // then
        System.out.println("refreshToken : " + refreshToken);
        assertNotNull(refreshToken);
    }

    @Test
    public void testIsTokenExpired() {
        // given
        String header = "Bearer " + ExpiredTokenGenerator.generateExpiredToken(dto, jwtSecretKey);

        // when
        assertThrows(RuntimeException.class, () -> jwtTokenProvider.resolveToken(header));
    }

    @Test
    public void testAccessTokenRefresh() {
        // given
<<<<<<< HEAD
        ReflectionTestUtils.setField(jwtTokenProvider, "accessTokenExpirationTime", -10);
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshTokenExpirationTime", 10);
        String header = "Bearer " + jwtTokenProvider.generateAccessToken(dto);
        String refreshToken = jwtTokenProvider.generateRefreshToken(dto);
=======
        String header = "Bearer " + ExpiredTokenGenerator.generateExpiredToken(dto, jwtSecretKey);
        String refreshToken = JwtTokenProvider.generateRefreshToken(dto);
        ReflectionTestUtils.setField(JwtTokenProvider, "refreshTokenExpirationTime", 1000000);
>>>>>>> 8a5f59484d2874654ae23e99e710b447ae6ae0d6

        // when
        System.out.println("=============== testAccessTokenRefresh ===============");
        String token = jwtTokenProvider.resolveToken(header);
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
        String token = jwtTokenProvider.generateAccessToken(dto);

        // when
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        // then
        System.out.println("userId : " + userId);
        assertEquals(dto.getId(), userId);
    }

    @Test
    public void testReceiveRoleFromToken() {
        // given
        String token = jwtTokenProvider.generateAccessToken(dto);

        // when
        RoleType role = jwtTokenProvider.getRoleFromToken(token);

        // then
        System.out.println("role : " + role);
        assertEquals(dto.getRole(), role);
    }

    @Test
    public void testReceiveUserIdFromToken() {
        // given
        String token = JwtTokenProvider.generateAccessToken(dto);

        // when
        Long userId = ReflectionTestUtils.invokeMethod(JwtTokenProvider, "getUserIdFromToken", token);

        // then
        System.out.println("userId : " + userId);
        assertEquals(dto.getId(), userId);
    }

    @Test
    public void testReceiveGithubIdFromToken() {
        // given
        String token = JwtTokenProvider.generateAccessToken(dto);

        // when
        Integer githubId = ReflectionTestUtils.invokeMethod(JwtTokenProvider, "getGithubIdFromToken", token);

        // then
        System.out.println("githubId : " + githubId);
        assertEquals(dto.getGithubId(), githubId);
    }

    private UserAuthenticateDto createDto() {
<<<<<<< HEAD
        return UserAuthenticateDto.of(1L, RoleType.USER);
=======
        return UserAuthenticateDto.of(1L, 1);
>>>>>>> 8a5f59484d2874654ae23e99e710b447ae6ae0d6
    }
}