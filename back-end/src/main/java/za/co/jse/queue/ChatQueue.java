package za.co.jse.queue;

import org.springframework.stereotype.Component;
import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.ChatUser;
import za.co.jse.exceptions.UserAlreadyExistsException;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class ChatQueue {

    private final BlockingQueue<ChatMessage> queue = new LinkedBlockingQueue<>();
    private final Map<String,ChatUser> users = new ConcurrentHashMap<>();
    /**
     * Publish a message to the queue
     * @param chatMessage - the message to publish
     * @throws InterruptedException - if the thread is interrupted while waiting to publish the message
     */
    public void publish(ChatMessage chatMessage) throws InterruptedException {
        //-- publish the message
        queue.put(chatMessage);
    }
    /**
     * Consume a message from the queue
     * @return - the consumed message
     * @throws InterruptedException - if the thread is interrupted while waiting to consume the message
     */
    public ChatMessage consume() throws InterruptedException {
        //-- consume the message
        return queue.take();
    }
    /**
     * Register a user in the chat room
     * @param user - the user to register
     * @return - the registered user
     */
    public ChatUser register(ChatUser user) {
        //-- change to lowercase
        String key = user.getUsername().toLowerCase();
        //-- add if the user does exists,
        ChatUser existing = users.putIfAbsent(key, user);
        //-- Do a check if the user exists or not,
        if (existing != null) {
            throw new UserAlreadyExistsException(user.getUsername());
        } else {
            users.put(user.getUsername(), user);
        }
        //-- return the user
        return user;
    }
    /**
     * Remove a user from the chat room
     * @param username - the username of the user to remove
     */
    public void remove(String username) {
        users.remove(username.toLowerCase());
    }
    /**
     * Get all users in the chat room
     * @return - a collection of users
     */
    public Collection<ChatUser> getUsers() {
        return users.values();
    }


}
