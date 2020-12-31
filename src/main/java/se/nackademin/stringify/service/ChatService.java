package se.nackademin.stringify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.nackademin.stringify.controller.response.ConnectionNotification;
import se.nackademin.stringify.controller.response.Meeting;
import se.nackademin.stringify.domain.ChatSession;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.domain.Profile;
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.exception.ConnectionLimitException;
import se.nackademin.stringify.exception.InvalidKeyException;
import se.nackademin.stringify.exception.ProfileNotFoundException;
import se.nackademin.stringify.repository.ChatSessionRepository;
import se.nackademin.stringify.repository.MessageRepository;
import se.nackademin.stringify.repository.ProfileRepository;
import se.nackademin.stringify.util.Key;

import javax.validation.Valid;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final MessageRepository messageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ProfileRepository profileRepository;

    private ChatSession getChatSession(UUID chatSessionGuid) throws ChatSessionNotFoundException {
        return chatSessionRepository.findByGuid(chatSessionGuid)
                .orElseThrow(() -> new ChatSessionNotFoundException(
                        String.format("No meetings with the id %s was found.", chatSessionGuid)
                ));
    }

    public Message storeMessage(UUID chatSessionId, @Valid Message message) throws ChatSessionNotFoundException {
        ChatSession chatSession = getChatSession(chatSessionId);

        message.setChatSession(chatSession);
        return messageRepository.save(message);
    }

    public ConnectionNotification storeProfileConnected(UUID chatSessionGuid, @Valid Profile profile)
            throws ChatSessionNotFoundException {
        ChatSession chatSession = getChatSession(chatSessionGuid);

        profile.setChatSession(chatSession);
        ProfileDto connectedProfile = profileRepository.save(profile).convertToDto();

        return new ConnectionNotification(connectedProfile, String.format("%s has connected to the meeting.", connectedProfile.getName()));
    }

    public ConnectionNotification removeProfileDisconnected(UUID chatSessionGuid, @Valid Profile profile)
            throws ProfileNotFoundException, ChatSessionNotFoundException {

        ChatSession chatSession = getChatSession(chatSessionGuid);
        long isDeleted = profileRepository.deleteByGuidAndChatSession_Id(profile.getGuid(), chatSession.getId());

        if (isDeleted == 0)
            throw new ProfileNotFoundException("Unable to find profile");

        ChatSession chatSessionAfterProfileDeletion = getChatSession(chatSessionGuid);

        if (chatSessionAfterProfileDeletion.getMessages().size() == 0) {
            chatSessionRepository.delete(chatSessionAfterProfileDeletion);
        }


        return new ConnectionNotification(profile.convertToDto(), String.format("%s has disconnected.", profile.getName()));

    }

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
}
