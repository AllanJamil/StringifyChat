package se.nackademin.stringify.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.util.DateConverter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
public class MessageDto {

    private UUID guid;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    private String from;

    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 1000)
    private String content;
    private String avatar;
    private String date;

    @Builder
    public MessageDto(UUID guid, String from, String content, String avatar, String date) {
        this.guid = guid;
        this.from = from;
        this.content = content;
        this.avatar = avatar;
        this.date = date;
    }

    public Message convertToEntity() {
        return Message.builder()
                .guid(this.guid)
                .from(this.from)
                .avatar(this.avatar)
                .content(this.content)
                .date(DateConverter.stringToDate(this.date))
                .build();
    }
}
