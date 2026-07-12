package za.co.jse.services;


import org.springframework.stereotype.Service;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.ChatUser;

@Service
public class UserService {

    public ChatUser login(String username, ChatRoom chatRoom) {
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
