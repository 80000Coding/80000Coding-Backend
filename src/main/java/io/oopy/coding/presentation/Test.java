package io.oopy.coding.presentation;

import io.oopy.coding.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequiredArgsConstructor
public class Test {

    private final TestService testService;

    /*
        유저의 이름을 현재 시간과 함께 반환.
        DB 연결 확인 테스트용 코드.
        TODO - 삭제
     */
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return testService.getNowUserName();
    }
}
