package se.nackademin.stringify.controller.api;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("api/meetings")
public class MeetingController {

    private final ChatService chatService;

    @PostMapping("new-meeting/{profile}")
    public Meeting newMeeting(@PathVariable @Valid ProfileDto profile) {
       return chatService.createNewMeeting(profile.convertToEntity());
    }

    @GetMapping("join-meeting")
    public ResponseEntity<?> joinMeeting(@RequestParam String key) {
        try {
            ChatSession chatSession = chatService.joinMeetingByKey(key);
            return ResponseEntity.ok(chatSession.convertToDto());
        } catch (ChatSessionNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ConnectionLimitException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage());
        }
    }
}
