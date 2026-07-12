package za.co.jse.services;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;
import za.co.jse.exceptions.InvalidMessageException;
import za.co.jse.exceptions.UserNotFoundException;
import za.co.jse.queue.ChatQueue;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements IMessageService {

    @Getter
    private final ChatRoom defaultChatRoom;
    private final ChatQueue chatQueue;
    private final UserService userService;

    /**
     *
     * @param username
     * @return
     */
    @Override
    public MessageRespDto findByUsername(String username) {
        final List<ChatMessage> chatMessages = defaultChatRoom
                .getChat().stream()
                .filter(chatMessage -> chatMessage.getUsername().equals(username))
                .collect(toList());
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
        publishToChatRoom(messageDto, chatMessage);
        //--
        chatQueue.publish(chatMessage);
        //--
        return chatMessage;
    }

    private void publishToChatRoom(MessageDto messageDto, ChatMessage chatMessage) {
        //--
        findByUsername(messageDto.getUsername())
                .getChatMessages()
                .add(chatMessage);
    }

    private void validateMessage(MessageDto messageDto) {
        if (messageDto.getUsername() == null || messageDto.getText() == null) {
            throw new InvalidMessageException("Username and message cannot be null");
        }
        if (!userService.existsByUsername(messageDto.getUsername(), defaultChatRoom)) {
            throw new UserNotFoundException(messageDto.getUsername());
        }
    }


}
