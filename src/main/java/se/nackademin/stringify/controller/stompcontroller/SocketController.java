package se.nackademin.stringify.controller.stompcontroller;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequiredArgsConstructor
public class SocketController {

    private int count = 0;

    private final SimpMessagingTemplate template;

    @MessageMapping("increase")
    public void increaseCount(MessageCount messageCount) {
        count++;
        String name = HtmlUtils.htmlEscape(messageCount.getName());

        template.convertAndSend("/topic/count", new MessageCount(name, count));
    }

}
