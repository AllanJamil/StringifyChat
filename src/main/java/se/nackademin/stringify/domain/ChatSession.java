package se.nackademin.stringify.domain;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import se.nackademin.stringify.dto.ChatSessionDto;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;


@Table(name = "chatsessions")
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
/***
 * Model for a single chat session containing id, guid, timestamp, key, connectUrl, a list of messages and profiles.
 */
public class ChatSession implements IConvertDto<ChatSessionDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID guid = UUID.randomUUID();
    @CreationTimestamp
    private Timestamp created;
    @Column(unique = true)
    private String key;
    private String connectUrl;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany(mappedBy = "chatSession", cascade = CascadeType.ALL)
    List<Message> messages;
    @OnDelete(action = OnDeleteAction.CASCADE)
    @OneToMany (mappedBy = "chatSession", cascade = {CascadeType.ALL, CascadeType.REMOVE}, fetch = FetchType.EAGER)
    List<Profile> profilesConnected;

    /***
     * Entity to data transfer object.
     * @return Message data transfer object
     */
    @Override
    public ChatSessionDto convertToDto() {
        return ChatSessionDto.builder()
                .guid(getGuid())
                .key(this.key)
                .connectUrl(this.connectUrl)
                .build();
    }
}
