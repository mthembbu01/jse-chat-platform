package za.co.jse.controllers;


import org.springframework.http.HttpStatus;
import za.co.jse.entities.Message;
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
    //-- http://localhost:8090/api/v1/chat

    //-- http://localhost:8080/api/v1/chat?username=<username>
    @GetMapping
    public MessageRespDto handleFindByUsername(@RequestParam String username) {
        return service.findByUsername(username);
    }

    //-- http://localhost:8080/api/v1/chat/add
    @PostMapping(path = "add")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Message handleAddMessage(@RequestBody MessageDto messageDto) {
        //--
        return service.addMessage(messageDto);
    }

}
