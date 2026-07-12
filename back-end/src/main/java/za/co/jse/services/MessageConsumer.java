package za.co.jse.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import za.co.jse.entities.ChatMessage;
import za.co.jse.queue.ChatQueue;

@Service
@RequiredArgsConstructor
public class MessageConsumer {
    private final ChatQueue queue;
    /**
     *
     */
    private final SimpMessagingTemplate messagingTemplate;

    @PostConstruct
    public void startConsumer() {
        //-- Create a new thread
        Thread.startVirtualThread(() -> {
            //-- Loop forever
            while (true) {
                //-- Consume the message from the queue
                try {
                    //-- Block until a message is available
                    ChatMessage chatMessage = queue.consume();
                    //-- Send the message to the client
                    messagingTemplate.convertAndSend(
                            "/default-chat-room",
                            chatMessage);

                } catch (InterruptedException ex) {
                    //-- Check if the thread was interrupted
                    Thread.currentThread().interrupt();
                }
            }
        });
    }
}
