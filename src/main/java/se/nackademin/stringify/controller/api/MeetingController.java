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
import se.nackademin.stringify.service.ChatService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/meetings")
public class MeetingController {

    private final ChatService chatService;

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
    public Meeting newMeeting(@RequestBody @Valid ProfileDto profile) {
       return chatService.createNewMeeting(profile.convertToEntity());
    }

    @ApiOperation(
            value = "Finds a chat session by Key",
            produces = "application/json",
            consumes = "application/json",
            notes = "Provide a key in order to get information about the chat session",
            response = ChatSessionDto.class
    )
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "OK: A chat session with the given key has been found.",
                    response = ChatSessionDto.class),
            @ApiResponse(code = 404, message = "Not found: A chat session with the given key could not be found."),
            @ApiResponse(code = 400, message = "Bad request: Invalid key."),
            @ApiResponse(code = 204, message = "No content: A chat session with the given key has been found but " +
                    "could not provide with information due to a connection limit.")
    })
    @GetMapping("join-meeting/key/{key}")
    public ChatSessionDto joinWithKey(@PathVariable String key) {
        try {
            return chatService.joinMeetingByKey(key).convertToDto();
        } catch (ChatSessionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ConnectionLimitException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        } catch (InvalidKeyException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @ApiOperation(
            value = "Finds a chat session by guid",
            produces = "application/json",
            consumes = "application/json",
            notes = "Provide a guid in order to get information about the chat session",
            response = ChatSessionDto.class
    )
    @ApiResponses( value = {
            @ApiResponse(code = 200, message = "OK: A chat session with the given guid has been found.",
                    response = ChatSessionDto.class),
            @ApiResponse(code = 404, message = "Not found: A chat session with the given guid could not be found."),
            @ApiResponse(code = 204, message = "No content: A chat session with the given guid has been found but " +
                    "could not provide with information due to a connection limit.")
    })
    @GetMapping("join-meeting/chat-id/{chatId}")
    public ChatSessionDto joinWithGuid(@PathVariable UUID chatId) {
        try {
            return chatService.joinMeetingByGuid(chatId).convertToDto();

        } catch (ChatSessionNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (ConnectionLimitException e) {
            throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
        }
    }

}
