package za.co.jse.queue;

import lombok.*;
import org.springframework.stereotype.Service;
import za.co.jse.entities.ChatMessage;

import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
public class MessageProducer {
    private final ChatQueue queue;
    /**
     *
     * @param message
     * @throws InterruptedException
     */
    public void send(ChatMessage message) throws InterruptedException {
        queue.publish(message);
    }
}
