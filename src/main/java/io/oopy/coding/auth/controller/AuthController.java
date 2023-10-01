package io.oopy.coding.auth.controller;

import io.oopy.coding.auth.service.LoginService;
import io.oopy.coding.auth.service.SignupService;
import io.oopy.coding.common.cookie.CookieUtil;
import io.oopy.coding.common.dto.ResponseDTO;
import io.oopy.coding.user.service.UserSearchService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

import static io.oopy.coding.common.jwt.AuthConstants.ACCESS_TOKEN;
import static io.oopy.coding.common.jwt.AuthConstants.REFRESH_TOKEN;

@RestController
@RequestMapping("/api/v1/login/oauth2")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final LoginService loginService;
    private final SignupService signupService;
    private final UserSearchService userSearchService;
    private final CookieUtil cookieUtil;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    //TODO : 프론트에서 진행 예정
    @Operation(summary = "OAuth 로그인", description = "github으로 로그인 합니다.")
    @GetMapping("/github")
    public void oauth2Login(HttpServletResponse response) throws IOException {
        String authorizeUrl = "https://github.com/login/oauth/authorize";
        String fullAuthorizeUrl = String.format("%s?client_id=%s", authorizeUrl, clientId);
        response.sendRedirect(fullAuthorizeUrl);
    }

    // TODO : 실제로는 다른 uri로 요청예정, 프론트와 협의 필요
    @Operation(summary = "OAuth2 로그인 후 로그인/회원가입 분기 조회", description = "githubId 존재유무에 따라 로그인 및 회원가입 인지를 알려주고 로그인의 경우 사용자 정보로 jwt를 생성하고, 회원가입의 경우 githubId로 jwt를 생성합니다.")
    @GetMapping("/code/github")
    public ResponseEntity<ResponseDTO> oauth2Redirected(@RequestParam("code") String authorizationCode,
                                                        HttpServletRequest request) throws Exception {

        String redirectUri = request.getRequestURL().toString();
        String accessToken = getAccessToken(authorizationCode, redirectUri);
        Integer githubId = getOauthUserId(accessToken);

        Map<String, String> tokens;
        ResponseCookie cookie;
        Map<String, String> data;

        if (userSearchService.isPresentByGithubId(githubId)) {
            tokens = loginService.login(githubId);
            cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()),  60 * 60 * 24 * 7);
            data = Map.of("action", "login");

        }
        else {
            tokens = signupService.generateSignupTokens(githubId);
            cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()),  60 * 60 * 24 * 7);
            data = Map.of("action", "signup");

        }

        ResponseDTO responseDto = new ResponseDTO("success", data);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .header(ACCESS_TOKEN.getValue(), tokens.get(ACCESS_TOKEN.getValue()))
                .body(responseDto);
    }

    private String getAccessToken(String code, String redirectUri) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String accessTokenUrl = "https://github.com/login/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                accessTokenUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        Map<String, String> response = responseEntity.getBody();
        String accessToken = response.get("access_token");

        return accessToken;
    }

    private Integer getOauthUserId(String accessToken) throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        String accessTokenUrl = "https://api.github.com/user";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                accessTokenUrl,
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        Map<String, ?> response = responseEntity.getBody();
        Integer githubId = (Integer) response.get("id");

        return githubId;
    }
}