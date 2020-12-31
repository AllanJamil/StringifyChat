package se.nackademin.stringify.controller.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PingController {

    @GetMapping("ping")
    @ApiOperation(
            value = "Wake up the server",
            notes = "Pings the sleeping server whenever client is used",
            response = String.class
    )
    public String pingServer() {
        return "pong";
    }
}
