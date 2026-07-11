package za.co.jse.services;


import za.co.jse.entities.ChatRoom;

public class UserUtil {

    private UserUtil() {
    }

    public static boolean existsByUsername(String username, ChatRoom defaultChatRoom) {
        return defaultChatRoom
                .getChat()
                .containsKey(username);
    }


}
