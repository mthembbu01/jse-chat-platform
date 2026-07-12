package za.co.jse.controllers;


import org.springframework.http.HttpStatus;
import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;
import za.co.jse.services.IMessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/chat")
public class MessageController {

    private final IMessageService service;

    public MessageController(IMessageService service) {
        this.service = service;
    }

    //-- http://localhost:8080/api/v1/chat?username=<username>
    @GetMapping
    public MessageRespDto handleFindByUsername(@RequestParam("username") String username) {
        return service.findByUsername(username);
    }

    //-- http://localhost:8080/api/v1/chat/send
    @PostMapping(path = "send")
    @ResponseStatus(value = HttpStatus.ACCEPTED)
    public ChatMessage handleSend(@RequestBody MessageDto messageDto) throws InterruptedException {
        //--
        return service.send(messageDto);
    }

}
