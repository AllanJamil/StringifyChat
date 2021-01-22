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

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
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

        message.setDate(new Timestamp(new Date().getTime()));
        message.setId(UUID.randomUUID());
        message.setGuid(UUID.randomUUID());
        message.setChatSession(chatSession);
        return messageRepository.save(message);
    }

    public ConnectionNotice storeProfileConnected(UUID chatSessionGuid, @Valid Profile profile)
            throws ChatSessionNotFoundException {
        ChatSession chatSession = getChatSession(chatSessionGuid);

        System.out.println(profile.getGuid());
        Optional<Profile> optionalProfile = profileRepository.findByGuid(profile.getGuid());
        ProfileDto connectedProfile;
        if (optionalProfile.isEmpty()) {
            //TODO: FIX BASE ENTITY
            profile.setId(UUID.randomUUID());
            profile.setChatSession(chatSession);
            connectedProfile = profileRepository.save(profile).convertToDto();
        } else
            connectedProfile = optionalProfile.get().convertToDto();

        Message message = Message.builder()
                .guid(UUID.randomUUID())
                .id(UUID.randomUUID())
                .chatSession(chatSession)
                .date(new Timestamp(new Date().getTime()))
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

        ChatSession updatedChatSession = chatSessionRepository.save(chatSession);
        if (updatedChatSession.getProfilesConnected().size() == 0) {
            chatSessionRepository.deleteById(updatedChatSession.getId());
        }

        Message message = Message.builder()
                .guid(UUID.randomUUID())
                .id(UUID.randomUUID())
                .chatSession(chatSession)
                .date(new Timestamp(new Date().getTime()))
                .avatar("disconnect")
                .sender("Notice")
                .content(profile.getName() + " has disconnected from the meeting.")
                .build();

        Message messageToSend = messageRepository.save(message);

        return new ConnectionNotice(profile.convertToDto(), messageToSend.convertToDto());
    }

    @Override
    public ChatSession getChatSession(UUID chatSessionGuid) throws ChatSessionNotFoundException {
        return chatSessionRepository.findByGuid(chatSessionGuid)
                .orElseThrow(() -> new ChatSessionNotFoundException(
                        String.format("No meetings with the id %s was found.", chatSessionGuid)
                ));
    }
}
