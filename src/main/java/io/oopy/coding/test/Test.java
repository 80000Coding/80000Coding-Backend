package io.oopy.coding.test;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@Tag(name = "테스트", description = "테스트용 API")
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class Test {

    private final TestService testService;

    /*
        유저의 이름을 현재 시간과 함께 반환.
        DB 연결 확인 테스트용 코드.
        TODO - 삭제
     */
    @Operation(summary = "유저 이름 반환", description = "유저 이름을 현재 시간과 함께 반환합니다.")
    @GetMapping("")
    @ResponseBody
    public String test() {
        return testService.getNowUserName();
    }
}
