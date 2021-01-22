package se.nackademin.stringify.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.UUID;

@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID guid = UUID.randomUUID();

    @CreationTimestamp
    private Timestamp created;

    public BaseEntity(UUID id, UUID guid) {
        this.id = id;
        this.guid = guid;
    }
}
