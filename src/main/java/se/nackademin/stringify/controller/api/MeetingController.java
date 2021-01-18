package se.nackademin.stringify.controller.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import se.nackademin.stringify.controller.response.Meeting;
import se.nackademin.stringify.dto.ChatSessionDto;
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.exception.ConnectionLimitException;
import se.nackademin.stringify.exception.InvalidKeyException;
import se.nackademin.stringify.service.EmailService;
import se.nackademin.stringify.service.MeetingService;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/meetings")
public class MeetingController {

    private final MeetingService meetingService;
    private final EmailService emailService;

    @ApiOperation(
            value = "Stores a new chat session with the connected profile.",
            produces = "application/json",
            consumes = "application/json",
            notes = "Provide a profile to store a new chat session with the given profile in order " +
                    "to connect to the meeting.",
            response = Meeting.class
    )
    @ApiResponse(code = 200, message = "A new meeting has been created with given profile")
    @PostMapping("new-meeting")
    @CrossOrigin
    public Meeting newMeeting(@RequestBody @Valid ProfileDto profile) {
        return meetingService.createNewMeeting(profile.convertToEntity());
    }

    @ApiOperation(
            value = "Finds a chat session by Key or Chat Id",
            produces = "application/json",
            consumes = "application/json",
            notes = "Provide either a key or a chat id in order to get information about the chat session",
            response = ChatSessionDto.class
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK: A chat session with the given key or chat id has been found.",
                    response = ChatSessionDto.class),
            @ApiResponse(code = 404, message = "Not found: A chat session with the given key or chat id could not be found."),
            @ApiResponse(code = 400, message = "Bad request: Invalid key or chat id."),
            @ApiResponse(code = 204, message = "No content: A chat session with the given key or chat id has been found but " +
                    "could not provide with information due to a connection limit.")
    })
    @GetMapping("join-meeting")
    public ChatSessionDto joinWithKey(@RequestParam(required = false) String key,
                                      @RequestParam(required = false, name = "chat-id") UUID chatId) {
        final String NO_PARAM_FOUND = "No key value or chat id was provided.";

        if (key == null && chatId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, NO_PARAM_FOUND);
        }

        try {
            if (key != null) {
                return meetingService.getChatSessionByKey(key).convertToDto();
            } else {
                return meetingService.getMeetingByGuid(chatId).convertToDto();
            }
        } catch (ChatSessionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ConnectionLimitException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        } catch (InvalidKeyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    //Quick test
    @PostMapping("invite")
    public void newEmail(@Email @RequestParam("email") String email, @RequestParam("name") String profileName) {
            emailService.sendInvitationEmail
                    (email, profileName, "https://stringify-chat.netlify.app/profile");
    }
}
