package se.nackademin.stringify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.nackademin.stringify.controller.response.ConnectionNotice;
import se.nackademin.stringify.domain.ChatSession;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.domain.Profile;
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.exception.ProfileNotFoundException;
import se.nackademin.stringify.repository.ChatSessionRepository;
import se.nackademin.stringify.repository.MessageRepository;
import se.nackademin.stringify.repository.ProfileRepository;

import javax.validation.Valid;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LiveCommunicationService implements IService {

    private final MessageRepository messageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ProfileRepository profileRepository;

    public Message storeMessage(UUID chatSessionId, @Valid Message message) throws ChatSessionNotFoundException {
        ChatSession chatSession = getChatSession(chatSessionId);

        message.setChatSession(chatSession);
        return messageRepository.save(message);
    }

    public ConnectionNotice storeProfileConnected(UUID chatSessionGuid, @Valid Profile profile)
            throws ChatSessionNotFoundException {
        ChatSession chatSession = getChatSession(chatSessionGuid);

        profile.setChatSession(chatSession);
        ProfileDto connectedProfile = profileRepository.save(profile).convertToDto();

        return new ConnectionNotice(connectedProfile,
                String.format("%s has connected to the meeting.",
                        connectedProfile.getName()));
    }

    public ConnectionNotice removeProfileDisconnected(UUID chatSessionGuid, @Valid Profile profile)
            throws ProfileNotFoundException, ChatSessionNotFoundException {

        ChatSession chatSession = getChatSession(chatSessionGuid);
        long isDeleted = profileRepository.deleteByGuidAndChatSession_Id(profile.getGuid(), chatSession.getId());

        if (isDeleted == 0)
            throw new ProfileNotFoundException("Unable to find profile");

        ChatSession chatSessionAfterProfileDeletion = getChatSession(chatSessionGuid);

        if (chatSessionAfterProfileDeletion.getProfilesConnected().size() == 0) {
            chatSessionRepository.delete(chatSessionAfterProfileDeletion);
        }

        return new ConnectionNotice(profile.convertToDto(),
                String.format("%s has disconnected.", profile.getName()));
    }

    @Override
    public ChatSession getChatSession(UUID chatSessionGuid) throws ChatSessionNotFoundException {
        return chatSessionRepository.findByGuid(chatSessionGuid)
                .orElseThrow(() -> new ChatSessionNotFoundException(
                        String.format("No meetings with the id %s was found.", chatSessionGuid)
                ));
    }
}
