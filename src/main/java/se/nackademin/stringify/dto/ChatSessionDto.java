package se.nackademin.stringify.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import se.nackademin.stringify.domain.ChatSession;

import java.util.UUID;

@Getter
@Setter
public class ChatSessionDto {

    private UUID guid;
    private String key;
    private String connectUrl;
    private String cipherKey;

    @Builder
    public ChatSessionDto(
            UUID guid,
            String key,
            String connectUrl,
            String cipherKey) {
        this.guid = guid;
        this.key = key;
        this.connectUrl = connectUrl;
        this.cipherKey = cipherKey;
    }

    public ChatSession convertToEntity() {
        return ChatSession.builder()
                .guid(this.guid)
                .key(this.key)
                .connectUrl(this.connectUrl)
                .build();
    }
}
