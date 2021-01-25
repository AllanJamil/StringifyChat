package se.nackademin.stringify.domain;

import lombok.*;
import se.nackademin.stringify.dto.ProfileDto;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "profiles")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Profile implements IConvertDto<ProfileDto> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID guid = UUID.randomUUID();;
    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;
    private String avatar;
    @ManyToOne(cascade = CascadeType.PERSIST)
    private ChatSession chatSession;

    @Override
    public ProfileDto convertToDto() {
        return ProfileDto.builder()
                .guid(getGuid())
                .name(this.name)
                .avatar(this.avatar)
                .build();
    }
}
