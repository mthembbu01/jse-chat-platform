package za.co.jse.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoom {
    public static final Comparator<ChatMessage> CHAT_MESSAGE_COMPARATOR = Comparator.comparing(ChatMessage::getTimestamp);

    private String name;
    private List<ChatUser> users = new ArrayList<>();
    private Queue<ChatMessage> chat = new PriorityQueue<>(CHAT_MESSAGE_COMPARATOR);

    public ChatRoom(String name) {
        this.name = name;
    }

}
