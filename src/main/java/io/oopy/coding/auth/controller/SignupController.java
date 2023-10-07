package io.oopy.coding.auth.controller;

import com.nimbusds.oauth2.sdk.SuccessResponse;
import io.oopy.coding.auth.service.SignupService;
import io.oopy.coding.common.cookie.CookieUtil;
import io.oopy.coding.common.dto.ResponseDTO;
import io.oopy.coding.domain.user.dto.UserSignupReq;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.jcip.annotations.Immutable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static io.oopy.coding.common.jwt.AuthConstants.REFRESH_TOKEN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class SignupController {
    private final SignupService signupService;
    private final CookieUtil cookieUtil;

    @Operation(summary = "새로운 회원에 대한 가입진행", description = "회원가입 후 제대로 추가되었다면 OK, 아니라면 BAD_REQUEST를 반환한다.")
    @Parameters({
            @Parameter(name = "Authorization", description = "OAuth2로 로그인 후 발급해준 토큰", in = ParameterIn.HEADER),
            @Parameter(name = "dto", description = "회원가입 정보", schema = @Schema(implementation = UserSignupReq.class))
    })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
            @ApiResponse(responseCode = "4xx", description = "에러", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO> signup(@RequestHeader("Authorization") String authorizationHeader,
                                              @Valid @RequestBody UserSignupReq dto,
                                              HttpServletRequest request, HttpServletResponse response) {

        Map<String, String> tokens = signupService.signup(dto, authorizationHeader);

        if (!tokens.isEmpty()) {
            ResponseDTO responseDto = new ResponseDTO("Signup Successful", null);
            ResponseCookie cookie = cookieUtil.createCookie(REFRESH_TOKEN.getValue(), tokens.get(REFRESH_TOKEN.getValue()), 60 * 60 * 24 * 7);

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(responseDto);
        }
        else {
            ResponseDTO responseDto = new ResponseDTO("Signup Failed", null);

            return ResponseEntity.badRequest()
                    .body(responseDto);
        }
    }
}
