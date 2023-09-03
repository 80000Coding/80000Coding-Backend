package io.oopy.coding.presentation.oauth2;

import io.oopy.coding.domain.repository.UserRepository;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import io.oopy.coding.global.jwt.JwtTokenProvider;
import io.oopy.coding.global.redis.blacklist.BlackListToken;
import io.oopy.coding.global.redis.blacklist.BlackListTokenRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping("/login/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {

    //private final

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    private final BlackListTokenRepository blackListTokenRepository;

    @Value("${spring.security.oauth2.client.registration.github.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.github.client-secret}")
    private String clientSecret;

    @Operation(summary = "OAuth 로그인", description = "github으로 로그인 합니다.")
    @GetMapping("/github")
    public void oauth2Login(HttpServletResponse response) throws IOException {
        String authorizeUrl = "https://github.com/login/oauth/authorize";
        String fullAuthorizeUrl = String.format("%s?client_id=%s", authorizeUrl, clientId);
        response.sendRedirect(fullAuthorizeUrl);
    }

    // TODO : 실제로는 다른 uri로 요청예정, 프론트와 협의 필요
    @Operation(summary = "OAuth2 로그인 후 로그인 및 회원가입인지 분기 안내", description = "githubId 존재유무에 따라 로그인 및 회원가입 인지를 알려주고 githubId로 JWT를 생성합니다.")
    @GetMapping("/code/github")
    public ResponseEntity<ResponseDto> oauth2Redirected(@RequestParam("code") String authorizationCode,
                                                        HttpServletResponse response) throws Exception {
        try {
            String accessToken = getAccessToken(authorizationCode);
            log.warn("Access Token {}", accessToken);
            String githubId = getOauthUserId(accessToken);
            log.warn("Github Id {}", githubId);

            ResponseDto responseDto = new ResponseDto();

            String code = userRepository.existsByGithubId(Integer.parseInt(githubId)) ? "log-in" : "sign-up";
            responseDto.setCode(code);
            // TODO : User가 존재하지 않을 시 githubId로 JWT 생성 return
            // TODO : User가 존재할 시 User로 JWT 생성 후 return
            // String jwt = userRepository.existsByGithubId(Integer.parseInt(githubId)) ? userJWT : githubJWT;

            //임시로 github Id로만 만드는걸로 구성
            UserAuthenticateDto userAuthenticateDto = UserAuthenticateDto.of(Long.parseLong(githubId));

            String userAccessToken = jwtTokenProvider.generateAccessToken(userAuthenticateDto);
            String userRefreshToken = jwtTokenProvider.generateRefreshToken(userAuthenticateDto);

            //cookie에 담아서 보낸다
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Access-Token", userAccessToken);
            responseHeaders.add("Refresh-Token", userRefreshToken);

            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(responseDto);
        }
        catch (Exception e) {
            ResponseDto badResponseDto = new ResponseDto();
            badResponseDto.setCode(e.getMessage());

            return ResponseEntity.badRequest()
                    .body(badResponseDto);
        }
    }

    private String getAccessToken(String code) throws Exception {
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

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                accessTokenUrl,
                HttpMethod.POST,
                requestEntity,
                Map.class
        );

        Map<String, String> response = responseEntity.getBody();
        String accessToken = response.get("access_token");

        if (accessToken == null)
            throw new Exception("No Access Token returned");
        else
            return accessToken;
    }

    private String getOauthUserId(String accessToken) throws Exception {
        log.warn("OAuth User 정보 요청");
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

        // TODO : response가 문제가 있을 시 예외 처리

        Map<String, String> response = responseEntity.getBody();
        String githubId = String.valueOf(response.get("id"));

        if (githubId == null)
            throw new Exception("No User Info returned");
        else
            return githubId;
    }

//discussion front jwt, request
//responsedBody + responseEntity
}

// exception 일어나면 그냥 error 라고 return 하기


//정적팩토리  of
//블랙리스트 토큰 관리 redis
