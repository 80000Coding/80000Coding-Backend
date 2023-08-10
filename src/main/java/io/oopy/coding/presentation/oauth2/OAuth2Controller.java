package io.oopy.coding.presentation.oauth2;

import io.oopy.coding.domain.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/login/oauth2")
@Slf4j
public class OAuth2Controller {

    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    public OAuth2Controller(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(summary = "OAuth 로그인", description = "github으로 로그인 합니다.")
    @GetMapping("/github")
    @ResponseBody
    public void oauth2Login(HttpServletResponse response) throws IOException {
        String authorizeUrl = "https://github.com/login/oauth/authorize";

        String fullAuthorizeUrl = String.format("%s?client_id=%s", authorizeUrl, clientId);

        response.sendRedirect(fullAuthorizeUrl);
    }

    // TODO : 실제로는 다른 uri로 요청예정, 프론트와 협의 필요
    @Operation(summary = "OAuth Access Token 요청", description = "github에 access token을 요청 합니다.")
    @GetMapping("/code/github")
    @ResponseBody
    public void oauth2Redirected(@RequestParam("code") String code, HttpServletResponse response) throws IOException {
        log.warn("OAuth Access Token 요청");
        RestTemplate restTemplate = new RestTemplate();

        String accessTokenUrl = "https://github.com/login/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");

        String redirectUri = "http://localhost:8081/login/oauth2/code/github";

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("code", code);
        body.add("redirect_uri", redirectUri);

        log.warn("request {}", body.toString());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                accessTokenUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        // TODO : response가 문제가 있을 시 예외 처리

        Map<String, String> responseBody = responseEntity.getBody();
        String accessToken = responseBody.get("access_token");

        log.warn("Response {}", responseBody.toString());
        log.warn("access token {}", accessToken);

        String githubId = getOauthUserId(accessToken);

        boolean userExists = userRepository.existsByGithubId(Integer.parseInt(githubId));

        // TODO : User가 존재하지 않을 시 githubId로 JWT 생성 return
        // TODO : User가 존재할 시 User로 JWT 생성 후 return

        if (userExists) {
            log.warn("User exists");
        }
        else {
            log.warn("User not exists");
        }
    }

    private String getOauthUserId(String accessToken) {
        log.warn("OAuth User 정보 요청");
        RestTemplate restTemplate = new RestTemplate();

        String accessTokenUrl = "https://api.github.com/user";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json");
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                accessTokenUrl,
                HttpMethod.GET,
                requestEntity,
                Map.class
        );

        // TODO : response가 문제가 있을 시 예외 처리

        Map<String, String> responseBody = response.getBody();
        String githubId = String.valueOf(responseBody.get("id"));

        log.warn("Response {}", responseBody.toString());
        log.warn("githubId {}", githubId);

        return githubId;
    }

}
