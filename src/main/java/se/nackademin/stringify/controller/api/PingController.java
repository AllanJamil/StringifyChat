package se.nackademin.stringify.controller.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("ping")
    public String pingServer() {
        return "pong";
    }
}
