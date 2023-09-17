package io.oopy.coding.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailConstant {
    public static final Integer EMAIL_EXPIRED_PERIOD_SECONDS = 600; // 임시 10분
    public static final String CERT_TITLE = String.format("[%s] 조직 이메일 인증", GlobalConstant.PROJECT_NAME);
    public static final String CERT_CONTENT = "<head>\n" +
            "<meta charset=\"UTF-8\">\n" +
            "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "<style>\n" +
            "  .link-button {\n" +
            "    display: inline-block;\n" +
            "    padding: 10px 20px;\n" +
            "    background-color: #3498db;\n" +
            "    color: #fff;\n" +
            "    text-align: center;\n" +
            "    text-decoration: none;\n" +
            "    border-radius: 5px;\n" +
            "    border: none;\n" +
            "    cursor: pointer;\n" +
            "    transition: background-color 0.3s;\n" +
            "    cursor: pointer\n"+
            "  }\n" +
            "  .text-color {\n" +
            "    color: #fff;\n" +
            "  }\n" +
            "\n" +
            "  .link-button:hover {\n" +
            "    background-color: #2980b9;\n" +
            "  }\n" +
            "</style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<a href=%s/email/cert/%s/%s/%s target=\"_blank\" class=\"link-button\"><div class=\"text-color\">인증하기</div></a>\n" +
            "\n" +
            "</body>\n" +
            "</html>";
}
