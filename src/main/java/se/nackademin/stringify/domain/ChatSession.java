package se.nackademin.stringify.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.nackademin.stringify.dto.ChatSessionDto;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;


@Table(name = "chatsessions")
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
    @OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL)
    List<Message> messages;
    @OneToMany (mappedBy = "chatSession", cascade = CascadeType.ALL)
    List<Profile> profilesConnected;

    @Builder
    public ChatSession(
            UUID id,
            UUID guid,
            String key,
            String connectUrl) {
        super(id, guid);
        this.key = key;
        this.connectUrl = connectUrl;
    }

    public ChatSessionDto convertToDto() {
        return ChatSessionDto.builder()
                .guid(getGuid())
                .key(this.key)
                .connectUrl(this.connectUrl)
                .build();
    }
}
