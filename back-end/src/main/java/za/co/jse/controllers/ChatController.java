package za.co.jse.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.services.IMessageService;

/**
 * WebSocket controller for handling real-time chat messages
 */
@Controller
@Slf4j
@RequiredArgsConstructor
public class ChatController extends BaseController {

    private final IMessageService messageService;

    /**
     * Handles incoming chat messages and persists them. The queue consumer is the
     * single publisher that broadcasts the message to `/topic/chat/messages`.
     *
     * @param messageDto The message data from client
     */
    @MessageMapping("/send")
    public void handleChatMessage(@Payload MessageDto messageDto) {
        log.info("Received message from user: {} - Text: {}", messageDto.getUsername(), messageDto.getText());

        try {
            messageService.send(messageDto);
            log.info("Message processed and queued for user: {}", messageDto.getUsername());
        } catch (Exception e) {
            log.error("Error processing message from user: {}", messageDto.getUsername(), e);
            throw new RuntimeException("Failed to process message: " + e.getMessage());
        }
    }

    /**
     * Handles notification messages
     *
     * @param notification The notification text from client
     * @return The notification to broadcast to all subscribers
     */
    @MessageMapping("/notification")
    @SendTo("/topic/chat/notifications")
    public String handleNotification(@Payload String notification) {
        log.info("Notification: {}", notification);
        return notification;
    }
}

