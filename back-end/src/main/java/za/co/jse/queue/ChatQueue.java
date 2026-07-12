package za.co.jse.queue;

import org.springframework.stereotype.Component;
import za.co.jse.entities.ChatMessage;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class ChatQueue {
    private final BlockingQueue<ChatMessage> queue = new LinkedBlockingQueue<>();

    public void publish(ChatMessage chatMessage) throws InterruptedException {
        //--
        queue.put(chatMessage);
    }
    /**
     *
     */
    public ChatMessage consume() throws InterruptedException {
        //--
        return queue.take();
    }

}
