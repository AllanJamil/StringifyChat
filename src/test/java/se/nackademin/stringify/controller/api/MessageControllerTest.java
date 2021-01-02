package se.nackademin.stringify.controller.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import se.nackademin.stringify.AbstractIntegrationTest;
import se.nackademin.stringify.domain.ChatSession;
import se.nackademin.stringify.domain.Message;
import se.nackademin.stringify.dto.MessageDto;
import se.nackademin.stringify.repository.ChatSessionRepository;
import se.nackademin.stringify.repository.MessageRepository;
import se.nackademin.stringify.util.Key;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MessageControllerTest extends AbstractIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MessageRepository messageRepository;

    @Autowired
    ChatSessionRepository chatSessionRepository;

    private ChatSession chatSession;

    @BeforeEach
    void setUp() {

        UUID guid = UUID.randomUUID();
        chatSession = ChatSession.builder()
                .key(Key.generate().toString())
                .guid(guid)
                .id(UUID.randomUUID())
                .connectUrl("https://stringify-chat.netlify.app/profile?connect=" + guid)
                .build();

        chatSession = chatSessionRepository.save(chatSession);

        for (int i = 0; i < 7; i++) {
            Message message = Message.builder()
                    .id(UUID.randomUUID())
                    .guid(UUID.randomUUID())
                    .avatar("avatar" + (i + 1))
                    .sender("name" + i)
                    .content("Whatever " + i)
                    .chatSession(chatSession)
                    .date(Timestamp.valueOf("2020-09-0" + (i + 1) + " 10:50:00.5464"))
                    .build();

            messageRepository.save(message);
        }

    }

    @DisplayName("Chat id with existing chat session should return a list of 5 messages")
    @Test
    void chatIdWithExistingChatSessionShouldReturnAListOf5MessageDtos() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/api/messages/history")
                .param("chatid", this.chatSession.getGuid().toString())
                .param("page", "0"))
                .andExpect(status().isOk())
                .andDo(print())
                .andReturn();

        List<MessageDto> messages =
                new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(),
                        new TypeReference<List<MessageDto>>() {});

        System.out.println(mvcResult.getResponse().getContentAsString());
        assertThat(messages).hasSize(5);

    }
}
