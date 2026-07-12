package za.co.jse.controllers;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import za.co.jse.entities.ChatUser;
import za.co.jse.services.UserService;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
public class UserController extends BaseController {

    private final UserService service;

    @GetMapping("{username}")
    public ChatUser login(@PathVariable("username") String username) {
        return service.login(username);

    }
}