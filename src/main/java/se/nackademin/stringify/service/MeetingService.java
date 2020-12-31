package se.nackademin.stringify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.nackademin.stringify.controller.response.Meeting;
import se.nackademin.stringify.domain.ChatSession;
import se.nackademin.stringify.domain.Profile;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.exception.ConnectionLimitException;
import se.nackademin.stringify.exception.InvalidKeyException;
import se.nackademin.stringify.repository.ChatSessionRepository;
import se.nackademin.stringify.repository.MessageRepository;
import se.nackademin.stringify.repository.ProfileRepository;
import se.nackademin.stringify.util.Key;

import javax.validation.Valid;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MeetingService implements IService {

    private final MessageRepository messageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ProfileRepository profileRepository;

    public Meeting createNewMeeting(@Valid Profile profile) {

        ChatSession savedChatSession = chatSessionRepository.save(new ChatSession());
        savedChatSession.setConnectUrl("https://stringify-chat.netlify.app/profile?connect=" + savedChatSession.getGuid());
        savedChatSession.setKey(Key.generate().toString());
        ChatSession meeting = chatSessionRepository.save(savedChatSession);

        //TODO: fix bug; BaseEntity to generate Guid for Profile class as well
        profile.setGuid(UUID.randomUUID());
        profile.setChatSession(meeting);
        Profile connectedProfile = profileRepository.save(profile);

        return new Meeting(connectedProfile.convertToDto(), meeting.convertToDto());
    }

    @Transactional(readOnly = true)
    public ChatSession joinMeetingByKey(String key) throws ChatSessionNotFoundException, ConnectionLimitException, InvalidKeyException {

        if (!Key.isValidKey(key))
            throw new InvalidKeyException();

        ChatSession chatSession = chatSessionRepository.findByKey(key)
                .orElseThrow(() -> new ChatSessionNotFoundException(
                        String.format("No meetings with the key %s was found.", key)));

        if (chatSession.getProfilesConnected().size() == 5)
            throw new ConnectionLimitException("Meeting has reached the maximum number of connections.");

        return chatSession;
    }

    @Transactional(readOnly = true)
    public ChatSession joinMeetingByGuid(UUID chatId) throws ChatSessionNotFoundException, ConnectionLimitException {
        ChatSession chatSession = getChatSession(chatId);

        if (chatSession.getProfilesConnected().size() == 5)
            throw new ConnectionLimitException("Meeting has reached the maximum number of connections.");

        return chatSession;
    }

    @Override
    public ChatSession getChatSession(UUID chatSessionGuid) throws ChatSessionNotFoundException {
        return chatSessionRepository.findByGuid(chatSessionGuid)
                .orElseThrow(() -> new ChatSessionNotFoundException(
                        String.format("No meetings with the id %s was found.", chatSessionGuid)
                ));
    }
}
