package se.nackademin.stringify.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.nackademin.stringify.dto.ChatSessionDto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.sql.Date;
import java.util.List;
import java.util.UUID;

import static se.nackademin.stringify.domain.ChatSession.Status.DEFAULT;


@Table(name = "chatsession")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class ChatSession extends BaseEntity{

    //TODO: ScheduledFuture schedule(Runnable task, Date startTime);
    // id for internal server use
    @Column(unique = true)
    private String key;
    private String connectUrl;
    private Date expirationDate;
    private Status status = DEFAULT;
    @OneToMany(mappedBy = "chatSession")
    List<Message> messages;

    @Builder
    public ChatSession(
            UUID id,
            UUID guid,
            String key,
            String connectUrl,
            Date expirationDate) {
        super(id, guid);
        this.key = key;
        this.connectUrl = connectUrl;
        this.expirationDate = expirationDate;
    }

    public ChatSessionDto convertToDto() {
        return ChatSessionDto.builder()
                .guid(getGuid())
                .key(this.key)
                .connectUrl(this.connectUrl)
                .expirationDate(this.expirationDate)
                .build();
    }

    public enum Status {
        DEFAULT,
        EXTENDED
    }
}
