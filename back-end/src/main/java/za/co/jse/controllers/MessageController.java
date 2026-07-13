package za.co.jse.controllers;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.dtos.MessageRespDto;
import za.co.jse.services.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/chat")
@RequiredArgsConstructor
public class MessageController extends BaseController{

    private final IMessageService service;

    //-- http://localhost:8080/api/v1/chat?username=<username>
    @GetMapping
    public MessageRespDto handleFindByUsername(@RequestParam("username") String username) {
        return service.findByUsername(username);
    }

    //-- http://localhost:8080/api/v1/chat/send
    @PostMapping(path = "send")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ChatMessage handleSend(@RequestBody ChatMessage chatMessage) throws InterruptedException {
        //--
        return service.send(chatMessage);
    }

    @GetMapping("default")
    public List<ChatMessage> getDefaultChat() {
        List<ChatMessage> defaultChat = service.getDefaultChat();
        return defaultChat;
    }

}
