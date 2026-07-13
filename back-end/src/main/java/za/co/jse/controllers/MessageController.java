package za.co.jse.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.ChatUser;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;
import za.co.jse.services.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
public class MessageController extends BaseController{

    private final IMessageService service;

    //-- http://localhost:8090/chat/api/v1?username=<username>
    @GetMapping
    public ChatUser handleJoin(@RequestParam("username") String username) {
        log.info("Join request received for username: {}", username);
        return service.join(username);

    }

    //-- http://localhost:8090/chat/api/v1/send
    @PostMapping(path = "send")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ChatMessage handleSend(@RequestBody MessageDto messageDto) throws InterruptedException {
        //--
        return service.send(messageDto);
    }

}
