package za.co.jse.services;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.Message;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;
import za.co.jse.exceptions.InvalidMessageException;
import za.co.jse.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements IMessageService {

    @Getter
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
        return new MessageRespDto(username, messages);
    }

    public void send(MessageDto messageDto) {
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

    private void validateMessage(MessageDto messageDto) {
        if (messageDto.getUsername() == null || messageDto.getText() == null) {
            throw new InvalidMessageException("Username and message cannot be null");
        }
        if (!UserUtil.existsByUsername(messageDto.getUsername(), defaultChatRoom)) {
            throw new UserNotFoundException(messageDto.getUsername());
        }
    }


}
