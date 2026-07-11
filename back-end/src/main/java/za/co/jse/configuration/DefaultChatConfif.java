package za.co.jse.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.Message;

import java.util.HashMap;
import java.util.List;

@Configuration
public class DefaultChatConfif {

    private final String chatRoomName = "default";

    @Bean
    public ChatRoom defaultChatRoom() {
        return new ChatRoom(chatRoomName, new HashMap<String, List<Message>>());
    }
}
