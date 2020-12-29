package se.nackademin.stringify.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.Date;
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
    private Date date;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ChatSession chatSession;

    public Message(UUID id, UUID guid, String from, String content, String avatar, Date date, ChatSession chatSession) {
        super(id, guid);
        this.from = from;
        this.content = content;
        this.avatar = avatar;
        this.date = date;
        this.chatSession = chatSession;
    }
}
