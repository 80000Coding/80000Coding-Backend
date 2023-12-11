package io.oopy.coding.common.email.dto;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailSend {

    private String targetEmail;
    @Setter
    private String title;
    @Setter
    private String content;

    public static EmailSend of(String targetEmail, String title, String content) {
        return new EmailSend(targetEmail,
                title,
                content
        );
    }
}
