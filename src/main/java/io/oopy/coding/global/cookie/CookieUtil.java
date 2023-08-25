package io.oopy.coding.global.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Optional;

@Component
public class CookieUtil {
    public Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .findAny();
    }

    public static Optional<String> readServletCookie(HttpServletRequest request, String cookieName) {
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findAny();
    }

    public ResponseCookie createCookie(String cookieName, String value, int maxAge) {
        return ResponseCookie.from(cookieName, value)
                .path("/")
                .httpOnly(true)
                .maxAge(maxAge)
                .secure(true)
                .sameSite("None")
                .build();
    }

    public void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .forEach(cookie -> {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setPath("/");
                    response.addCookie(cookie);
                });
    }
}
