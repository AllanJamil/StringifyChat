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
    private String Sender;

    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 1000)
    private String content;
    private String picturePath;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ChatSession chatSession;

    public Message(UUID id, UUID guid, String sender,  String content, String picturePath, ChatSession chatSession) {
        super(id, guid);
        Sender = sender;
        this.content = content;
        this.picturePath = picturePath;
        this.chatSession = chatSession;
    }
}
