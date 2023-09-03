package io.oopy.coding.presentation.signup;

import io.oopy.coding.domain.dto.ResponseDTO;
import io.oopy.coding.domain.user.dto.UserAuthenticateDto;
import io.oopy.coding.global.jwt.JwtTokenProvider;
import io.oopy.coding.presentation.oauth2.ResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SignupController {

    private final SignupService signupService;

    @Operation(summary = "새로운 회원에 대한 가입진행", description = "회원가입 후 제대로 추가되었다면 OK, 아니라면 BAD_REQUEST를 반환한다.")
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignupRequest request,
                                 @RequestHeader("Authorization") String authorizationHeader) {
        boolean success = signupService.signup(request.getName(), request.getEmail(), authorizationHeader);

        //어떤 경우든 해당 토큰을 블랙리스트로 관리한다
        //BlackListToken blackListToken = BlackListToken.of(signUpJwt, userId, ttl);
        //blackListTokenRepository.save(blackListToken);

        //clear cookie

        if (success) {
            return new ResponseEntity<>("Signup Successful", HttpStatus.OK);
            //User 정보로 jwt 생성 후 header에 적용
//        UserAuthenticateDto userAuthenticateDto = UserAuthenticateDto.of(Long.parseLong(githubId));
//
//        String userAccessToken = jwtTokenProvider.generateAccessToken(userAuthenticateDto);
//        String userRefreshToken = jwtTokenProvider.generateRefreshToken(userAuthenticateDto);
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//        responseHeaders.add("Access-Token", userAccessToken);
//        responseHeaders.add("Refresh-Token", userRefreshToken);
        }
        else {
            return new ResponseEntity<>("Signup Failed", HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/testDTO")
    public ResponseEntity<ResponseDTO> testDTO() {
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("post", 1);
        responseData.put("title", "test title");

        ResponseDTO responseDTO= new ResponseDTO("test", responseData);

        return ResponseEntity.ok()
                .body(responseDTO);
    }
}
