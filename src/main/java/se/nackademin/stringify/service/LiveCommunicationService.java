package se.nackademin.stringify.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
import se.nackademin.stringify.util.DateUtil;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LiveCommunicationService implements IService {

    private final MessageRepository messageRepository;
    private final ChatSessionRepository chatSessionRepository;
    private final ProfileRepository profileRepository;

    public Message storeMessage(UUID chatSessionId, @Valid Message message) throws ChatSessionNotFoundException {
        ChatSession chatSession = getChatSession(chatSessionId);

        message.setDate(DateUtil.now());
        message.setId(UUID.randomUUID());
        message.setGuid(UUID.randomUUID());
        message.setChatSession(chatSession);
        return messageRepository.save(message);
    }

    public ConnectionNotice storeProfileConnected(UUID chatSessionGuid, @Valid Profile profile)
            throws ChatSessionNotFoundException {
        ChatSession chatSession = getChatSession(chatSessionGuid);

        Optional<Profile> optionalProfile = profileRepository.findByGuid(profile.getGuid());
        ProfileDto connectedProfile;
        if (optionalProfile.isEmpty()) {
            profile.setId(UUID.randomUUID());
            profile.setChatSession(chatSession);
            connectedProfile = profileRepository.save(profile).convertToDto();
        } else
            connectedProfile = optionalProfile.get().convertToDto();

        Message message = Message.builder()
                .guid(UUID.randomUUID())
                .id(UUID.randomUUID())
                .chatSession(chatSession)
                .date(DateUtil.now())
                .avatar("connect")
                .sender("Notice")
                .content(connectedProfile.getName() + " has connected to the meeting.")
                .build();

        Message messageToSend = messageRepository.save(message);


        return new ConnectionNotice(connectedProfile, messageToSend.convertToDto());
    }

    @Transactional
    public ConnectionNotice removeProfileDisconnected(UUID chatSessionGuid, @Valid Profile profile)
            throws ProfileNotFoundException, ChatSessionNotFoundException {

        Profile profileFound = profileRepository.findAllByGuidAndChatSession_Guid(profile.getGuid(), chatSessionGuid)
                .orElseThrow(() -> new ProfileNotFoundException("Could not find a profile"));
        ChatSession chatSession = getChatSession(chatSessionGuid);

        chatSession.getProfilesConnected().remove(profileFound);
        profileRepository.deleteById(profileFound.getId());

        boolean deleted = false;
        if (chatSession.getProfilesConnected().size() == 0) {
            deleted = true;
            chatSessionRepository.deleteById(chatSession.getId());
        }

        Message message;
        if (!deleted) {
            message = Message.builder()
                    .guid(UUID.randomUUID())
                    .id(UUID.randomUUID())
                    .chatSession(chatSession)
                    .date(DateUtil.now())
                    .avatar("disconnect")
                    .sender("Notice")
                    .content(profile.getName() + " has disconnected from the meeting.")
                    .build();
            message = messageRepository.save(message);
            return new ConnectionNotice(profile.convertToDto(), message.convertToDto());
        }

        return null;
    }

    @Override
    public ChatSession getChatSession(UUID chatSessionGuid) throws ChatSessionNotFoundException {
        return chatSessionRepository.findByGuid(chatSessionGuid)
                .orElseThrow(() -> new ChatSessionNotFoundException(
                        String.format("No meetings with the id %s was found.", chatSessionGuid)
                ));
    }
}
