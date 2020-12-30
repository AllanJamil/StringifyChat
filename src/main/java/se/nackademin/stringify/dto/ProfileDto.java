package se.nackademin.stringify.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import se.nackademin.stringify.domain.Profile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
public class ProfileDto {

    private UUID guid;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;
    private String avatar;
    private String date;

    @Builder
    public ProfileDto(UUID guid, String name, String avatar, String date) {
        this.guid = guid;
        this.name = name;
        this.avatar = avatar;
        this.date = date;
    }

    public Profile convertToEntity() {
        return Profile.builder()
                .guid(this.guid)
                .name(this.name)
                .avatar(this.avatar)
                .build();
    }
}
