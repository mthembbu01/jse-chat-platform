package za.co.jse.services;


import lombok.RequiredArgsConstructor;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.Message;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements IMessageService {

    private final ChatRoom defaultChatRoom;
    /**
     *
     * @param username
     * @return
     */
    @Override
    public MessageRespDto findByUsername(String username) {
        final List<Message> messages = defaultChatRoom
                .getChat()
                .computeIfAbsent(username, k -> new ArrayList<>());
        return new MessageRespDto(username,messages);
    }

    public void addMessage(MessageDto messageDto) {
        //-- Validate the message
        validateMessage(messageDto);
        //--
        final Message message = Message.builder()
                .username(messageDto.getUsername())
                .text(messageDto.getText())
                .timestamp(LocalDateTime.now())
                .build();
        //--
        findByUsername(messageDto.getUsername())
                .getMessages()
                .add(message);
    }

    private static void validateMessage(MessageDto messageDto) {
        if (messageDto.getUsername() == null || messageDto.getText() == null) {
            throw new RuntimeException("Username and message cannot be null");
        }
    }


}
