package za.co.jse.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoom {
    private String name;
    private Map<String, List<ChatMessage>> chat = new HashMap<>();

}
