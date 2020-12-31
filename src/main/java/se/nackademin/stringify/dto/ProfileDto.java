package se.nackademin.stringify.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "A object used for a client to be identified with during a chat session")
public class ProfileDto {

    @ApiModelProperty(notes = "An id used by the client side")
    private UUID guid;

    @NotEmpty
    @NotBlank
    @Size(min = 3, max = 30)
    @ApiModelProperty(required = true, notes = "The name of the profile user", example = "Jake Fish")
    private String name;
    @ApiModelProperty(notes = "A avatar used by the client-side", example = "avatar20")
    private String avatar;
    @ApiModelProperty(notes = "A date for presenting a connection notice to other users")
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
