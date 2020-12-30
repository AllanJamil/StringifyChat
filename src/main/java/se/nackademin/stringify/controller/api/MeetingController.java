package se.nackademin.stringify.controller.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.nackademin.stringify.controller.response.Meeting;
import se.nackademin.stringify.domain.ChatSession;
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.exception.ConnectionLimitException;
import se.nackademin.stringify.service.ChatService;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Api
@RequestMapping("api/meetings")
public class MeetingController {

    private final ChatService chatService;

    @ApiOperation(
            value = "Stores a new session of meeting with the connected profile.",
            produces = "application/json",
            consumes = "application/json",
            notes = "Provide a profile to store a new chat session with the given profile in order to connect to the meeting.",
            response = Meeting.class

    )
    @ApiResponse(code = 200, message = "A new meeting has been created with given profile")
    @PostMapping("new-meeting")
    public Meeting newMeeting(@RequestBody @Valid ProfileDto profile) {
       return chatService.createNewMeeting(profile.convertToEntity());
    }

    @GetMapping("join-meeting/{key}")
    public ResponseEntity<?> joinWithKey(@PathVariable String key) {
        try {
            ChatSession chatSession = chatService.joinMeetingByKey(key);
            return ResponseEntity.ok(chatSession.convertToDto());
        } catch (ChatSessionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ConnectionLimitException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }

    @GetMapping("join-meeting/{chatId}")
    public ResponseEntity<?> joinWithGuid(@PathVariable UUID chatId) {
        try {
            ChatSession chatSession = chatService.joinMeetingByGuid(chatId);
            return ResponseEntity.ok(chatSession.convertToDto());
        } catch (ChatSessionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ConnectionLimitException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }
}
