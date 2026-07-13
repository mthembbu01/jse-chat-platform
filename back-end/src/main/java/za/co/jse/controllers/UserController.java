package za.co.jse.controllers;


import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.ChatUser;
import za.co.jse.services.UserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService service;
    private final ChatRoom defaultChatRoom;

    @PostConstruct
    private void saveDummyData() {
        final LocalDateTime todayNineAm = LocalDate.now().atStartOfDay().withHour(9);
        List.of(
                ChatMessage.builder().username("System").text("Welcome to the Global Workspace Main Lobby channel.").timestamp(todayNineAm.withMinute(0).withSecond(0)).build(),
                ChatMessage.builder().username("Sarah_99").text("Hey guys! This dark-mode workspace is working flawlessly.").timestamp(todayNineAm.withMinute(14).withSecond(0)).build(),
                ChatMessage.builder().username("JohnDoe").text("WAgreed, the styling looks exactly like Messenger!").timestamp(todayNineAm.withMinute(0).withSecond(15)).build()
        ).forEach(chatMessage -> {
            login(chatMessage.getUsername());
            defaultChatRoom.getChat().add(chatMessage);
        });
        log.info("saved default chats");
    }

    @GetMapping("{username}")
    public ChatUser login(@PathVariable("username") String username) {
        return service.login(username);

    }
}