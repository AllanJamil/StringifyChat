package se.nackademin.stringify.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import se.nackademin.stringify.dto.ChatSessionDto;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.UUID;

import static se.nackademin.stringify.domain.ChatSession.Status.DEFAULT;


@Entity(name = "chatsessions")
@Getter
@Setter
@NoArgsConstructor
public class ChatSession {

    //TODO: ScheduledFuture schedule(Runnable task, Date startTime);
    // id for internal server use
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID guid;

    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp created;


    private String key;
    private String connectUrl;
    private Date expirationDate;
    private Status status = DEFAULT;

    @Builder
    public ChatSession(
            UUID guid,
            String key,
            String connectUrl,
            Date expirationDate) {
        this.guid = guid;
        this.key = key;
        this.connectUrl = connectUrl;
        this.expirationDate = expirationDate;
    }

    public ChatSessionDto convertToDto() {
        return ChatSessionDto.builder()
                .guid(this.guid)
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
