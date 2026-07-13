package za.co.jse.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.ChatUser;
import za.co.jse.exceptions.UserAlreadyExistsException;
import za.co.jse.queue.ChatQueue;

import java.util.PriorityQueue;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ChatQueue chatRoom;

    public ChatUser join(String username) {
        if (existsByUsername(username, chatRoom)) {
            throw new UserAlreadyExistsException(username);
        }

        ChatUser user = new ChatUser(
                username,
                new PriorityQueue<>(ChatUser.CHAT_MESSAGE_COMPARATOR));

        chatRoom.register(user);

        return user;
    }

    private ChatUser findByUsernameOrCreateNew(String username, ChatQueue chatRoom) {
        return chatRoom
                .getUsers()
                .stream()
                .filter(existingUser -> existingUser.getUsername().equals(username))
                .findFirst()
                .orElse(createNewUser(username, chatRoom));
    }

    private ChatUser createNewUser(String username, ChatQueue chatRoom) {
        final ChatUser user = new ChatUser(username, new PriorityQueue<>(ChatUser.CHAT_MESSAGE_COMPARATOR));
        // Register the user via ChatQueue API instead of mutating the internal map view
        chatRoom.register(user);

        return user;
    }

    public boolean existsByUsername(String username, ChatQueue chatRoom) {
        return chatRoom
                .getUsers()
                .stream()
                .anyMatch(existingUser -> existingUser.getUsername().equals(username));
    }

}
