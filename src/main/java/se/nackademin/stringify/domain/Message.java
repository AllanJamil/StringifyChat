package se.nackademin.stringify.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.nackademin.stringify.dto.MessageDto;
import se.nackademin.stringify.util.DateConverter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "message")
@NoArgsConstructor
public class Message extends BaseEntity{

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    private String from;

    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 1000)
    private String content;
    private String avatar;
    private Timestamp date;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ChatSession chatSession;

    @Builder
    public Message(UUID id, UUID guid, String from, String content, String avatar, Timestamp date, ChatSession chatSession) {
        super(id, guid);
        this.from = from;
        this.content = content;
        this.avatar = avatar;
        this.date = date;
        this.chatSession = chatSession;
    }

    public MessageDto convertToDto() {
        return MessageDto.builder()
                .guid(this.getGuid())
                .from(this.from)
                .avatar(this.avatar)
                .date(DateConverter.dateToString(this.date))
                .content(this.content)
                .guid(this.getGuid())
                .build();
    }
}
