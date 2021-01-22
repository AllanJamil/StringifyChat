package se.nackademin.stringify.controller.api;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:3000", "https://stringify-chat.netlify.app"})
public class PingController {

    @GetMapping("api/ping")
    @ApiOperation(
            value = "Wake up the server",
            notes = "Pings the sleeping server whenever client is used",
            response = String.class
    )
    public String pingServer() {
        return "Server started...";
    }
}
