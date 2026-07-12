package za.co.jse.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.ChatUser;

@Service
@RequiredArgsConstructor
public class UserService {

    private final ChatRoom chatRoom;

    public ChatUser login(String username) {
        return findByUsernameOrCreateNew(username, chatRoom);
    }

    private ChatUser findByUsernameOrCreateNew(String username, ChatRoom chatRoom) {
        return chatRoom
                .getUsers()
                .stream()
                .filter(existingUser -> existingUser.getUsername().equals(username))
                .findFirst()
                .orElse(createNewUser(username, chatRoom));
    }

    private ChatUser createNewUser(String username, ChatRoom chatRoom) {
        final ChatUser user = new ChatUser(username);

        chatRoom.getUsers().add(user);

        return user;
    }

    public boolean existsByUsername(String username, ChatRoom chatRoom) {
        return chatRoom
                .getUsers()
                .stream()
                .anyMatch(existingUser -> existingUser.getUsername().equals(username));
    }

}
