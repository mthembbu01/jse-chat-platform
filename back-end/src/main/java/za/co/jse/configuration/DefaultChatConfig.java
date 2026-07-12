package za.co.jse.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.ChatMessage;

import java.util.HashMap;
import java.util.List;

@Configuration
public class DefaultChatConfig {

    private final String chatRoomName = "default";

    @Bean
    public ChatRoom defaultChatRoom() {
        return new ChatRoom(chatRoomName, new HashMap<String, List<ChatMessage>>());
    }
}
