package za.co.jse.services;


import lombok.RequiredArgsConstructor;
import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.jse.queue.ChatQueue;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements IMessageService {

    private final ChatRoom defaultChatRoom;
    private final ChatQueue chatQueue;
    /**
     *
     * @param username
     * @return
     */
    @Override
    public MessageRespDto findByUsername(String username) {
        final List<ChatMessage> chatMessages = defaultChatRoom
                .getChat()
                .computeIfAbsent(username, k -> new ArrayList<>());
        return new MessageRespDto(username, chatMessages);
    }

    public ChatMessage send(MessageDto messageDto) throws InterruptedException {
        //-- Validate the chatMessage
        validateMessage(messageDto);
        //--
        final ChatMessage chatMessage = ChatMessage.builder()
                .username(messageDto.getUsername())
                .text(messageDto.getText())
                .timestamp(LocalDateTime.now())
                .build();
        //--
         findByUsername(messageDto.getUsername())
                .getChatMessages()
                .add(chatMessage);
         //--
        chatQueue.publish(chatMessage);
        //--
        return chatMessage;
    }

    private static void validateMessage(MessageDto messageDto) {
        if (messageDto.getUsername() == null || messageDto.getText() == null) {
            throw new RuntimeException("Username and message cannot be null");
        }
    }


}
