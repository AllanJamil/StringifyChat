package se.nackademin.stringify.controller.websocket;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.dto.MessageDto;
import se.nackademin.stringify.dto.ProfileDto;
import se.nackademin.stringify.exception.ChatSessionNotFoundException;
import se.nackademin.stringify.service.LiveCommunicationService;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class LiveCommunicationControllerTest {

    @InjectMocks
    LiveCommunicationController liveCommunicationController;

    @Mock
    LiveCommunicationService liveCommunicationService;

    private ProfileDto mockProfileDto;

    private MessageDto mockMessageDto;

    @BeforeEach
    void setUp() {
        mockProfileDto = ProfileDto.builder()
                .date("2021-01-01 16:45")
                .guid(UUID.randomUUID())
                .name("John Doe")
                .avatar("avatar1")
                .build();

        mockMessageDto = MessageDto.builder()
                .date("2021-01-02 09:53")
                .content("Test")
                .from("John Doe")
                .avatar("avatar1")
                .build();
    }

    @Test
    void testTransmitWithValidMessageShouldReturnMessageDto() throws ChatSessionNotFoundException {


        given(liveCommunicationService.storeMessage(any(UUID.class), any(Message.class))).willReturn(mockMessageDto.convertToEntity());

        assertThat(liveCommunicationController.transmit(UUID.randomUUID(), mockMessageDto))
                .isInstanceOf(MessageDto.class);


        then(liveCommunicationService).should(times(1)).storeMessage(any(UUID.class), any(Message.class));
    }

    @Test
    void testTransmitMessageWithNoDateShouldThrowIllegalArgumentException() throws ChatSessionNotFoundException {
        mockMessageDto.setDate(null);

        assertThatThrownBy(() -> liveCommunicationController.transmit(UUID.randomUUID(), mockMessageDto)).isInstanceOf(IllegalArgumentException.class);

        then(liveCommunicationService).should(times(0)).storeMessage(any(UUID.class), any(Message.class));
    }

    @Test
    void testTransmitMessageWithNoSenderNameShouldThrowNullPointerException() throws ChatSessionNotFoundException {
        mockMessageDto.setFrom(null);

        assertThatThrownBy(() -> liveCommunicationController.transmit(UUID.randomUUID(), mockMessageDto)).isInstanceOf(NullPointerException.class);

        then(liveCommunicationService).should(times(1)).storeMessage(any(UUID.class), any(Message.class));
    }

    @Test
    void chatIdWithNonExistingChatSessionShouldThrowResponseStatusException() throws ChatSessionNotFoundException {
        given(liveCommunicationService.storeMessage(any(UUID.class), any(Message.class))).willThrow(ChatSessionNotFoundException.class);

        assertThatThrownBy(() -> liveCommunicationController.transmit(UUID.randomUUID(), mockMessageDto))
                .isInstanceOf(ResponseStatusException.class);

        then(liveCommunicationService).should(times(1)).storeMessage(any(UUID.class), any(Message.class));
    }

/*    @Test
    void notifyOnConnect() {

    }

    @Test
    void notifyOnDisconnect() {
    }*/
}
