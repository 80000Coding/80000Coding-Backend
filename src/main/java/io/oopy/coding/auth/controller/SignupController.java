package io.oopy.coding.auth.controller;

import io.oopy.coding.auth.service.SignupService;
import io.oopy.coding.common.cookie.CookieUtil;
import io.oopy.coding.common.dto.ResponseDTO;
import io.oopy.coding.domain.user.dto.UserSignupReq;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.oopy.coding.common.jwt.AuthConstants.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class SignupController {
    private final SignupService signupService;
    private final CookieUtil cookieUtil;

    @Operation(summary = "새로운 회원에 대한 가입진행", description = "회원가입 후 제대로 추가되었다면 OK, 아니라면 BAD_REQUEST를 반환한다.")
    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> signup(@CookieValue("refreshToken") String refreshToken,
                                              @RequestHeader("Authorization") String authorizationHeader,
                                              @RequestBody UserSignupReq dto,
                                              HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> tokens = signupService.signup(dto, refreshToken, authorizationHeader);

        ResponseCookie cookie = cookieUtil.deleteCookie(request, response, REFRESH_TOKEN.getValue())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 쿠키입니다."));

        if (!tokens.isEmpty()) {
            ResponseDTO responseDto = new ResponseDTO("Signup Successful", null);
            cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()), 60 * 60 * 24 * 7);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(responseDto);
        }
        else {
            ResponseDTO responseDto = new ResponseDTO("Signup Failed", null);

            return ResponseEntity.badRequest()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(responseDto);
        }
    }
}
