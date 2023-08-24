package io.oopy.coding.email.service.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailSend {

    private String targetEmail;
    private String githubId;
    private String title;
    private String content;

    public static EmailSend of(String targetEmail, String githubId, String title, String content) {
        return new EmailSend(targetEmail,
                githubId,
                title,
                content
        );
    }
}
