package se.nackademin.stringify.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.util.DateConverter;

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
@Table(name = "profiles")
@NoArgsConstructor
public class Profile extends BaseEntity{

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;
    private String avatar;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ChatSession chatSession;

    @Builder
    public Profile(UUID id, UUID guid, String name, String avatar, ChatSession chatSession) {
        super(id, guid);
        this.name = name;
        this.avatar = avatar;
        this.chatSession = chatSession;
    }

    public ProfileDto convertToDto() {
        return ProfileDto.builder()
                .guid(this.getGuid())
                .name(this.name)
                .avatar(this.avatar)
                .date(DateConverter.dateToString(getCreated()))
                .build();
    }

}
