package za.co.jse.controllers;


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

    //-- http://localhost:8080/api/v1/customer/
    @GetMapping(path = "/{username}")
    public MessageRespDto handleFindByUsername(@PathVariable String username) {
        return service.findByUsername(username);
    }

}
