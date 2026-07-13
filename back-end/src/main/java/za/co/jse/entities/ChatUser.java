package za.co.jse.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatUser {
    //-- Compare the messages by timestamp
    public static final Comparator<ChatMessage> CHAT_MESSAGE_COMPARATOR = Comparator.comparing(ChatMessage::getTimestamp);
    private String username;
    private Queue<ChatMessage> messages = new PriorityQueue<>(CHAT_MESSAGE_COMPARATOR);
}
