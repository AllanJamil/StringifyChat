package se.nackademin.stringify.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.util.DateConverter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ApiModel(description = "A message object to be published to users connected to a chat session")
public class MessageDto {

    @ApiModelProperty(notes = "An id used by the client side")
    private UUID guid;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    @ApiModelProperty(notes = "The name of the user who sent the message", example = "John Doe")
    private String from;

    @NotBlank
    @NotEmpty
    @NotNull
    @Size(min = 3, max = 1000)
    @ApiModelProperty(notes = "The content of the message", example = "Hello there!", required = true)
    private String content;
    @ApiModelProperty(notes = "The avatar to be displayed on client-side. Avatars to be used are avatar1 - avatar20",
            example = "avatar1")
    private String avatar;
    @ApiModelProperty(notes = "The timestamp of the time of sending the message", example = "2021-01-01 14:32")
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
                .sender(this.from)
                .avatar(this.avatar)
                .content(this.content)
                .date(DateConverter.stringToDate(this.date))
                .build();
    }
}
