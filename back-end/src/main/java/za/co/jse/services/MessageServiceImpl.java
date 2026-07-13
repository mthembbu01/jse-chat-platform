package za.co.jse.services;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.ChatUser;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;
import za.co.jse.exceptions.InvalidMessageException;
import za.co.jse.queue.MessageConsumer;
import za.co.jse.queue.MessageProducer;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements IMessageService {

    private final MessageProducer producer;
    private final MessageConsumer messageConsumer;
    private final ChatRoom defaultChatRoom;
    private final UserService userService;

    /**
     * Join a chat room
     * @param username - the username of the user to join
     * @return - the joined user
     */
    @Override
    public ChatUser join(String username) {
        return userService.join(username);
    }
    /**
     * Find messages by username
     * @param username - the username of the user whose messages to find
     * @return - the messages for the specified user
     */
    @Override
    public MessageRespDto findByUsername(String username) {
        final List<ChatMessage> chatMessages = defaultChatRoom
                .getChat()
                .stream()
                .filter(chatMessage -> chatMessage.getUsername().equals(username))
                .collect(toList());
        return new MessageRespDto(username, chatMessages);
    }

    /**
     * Send a message to the chat room
     * @param messageDto - the message to send
     * @return - the sent message
     * @throws InterruptedException - if the thread is interrupted while waiting to send the message
     */
    public ChatMessage  send(MessageDto messageDto) throws InterruptedException {
        //-- Validate the chatMessage
        validateMessage(messageDto);
        //--
        final ChatMessage chatMessage = ChatMessage.builder()
                .username(messageDto.getUsername())
                .text(messageDto.getText())
                .timestamp(LocalDateTime.now())
                .build();
        //--
        producer.send(chatMessage);
        //--
        return chatMessage;
    }
    /**
     * Validate the message
     * @param messageDto - the message to validate
     */
    private void validateMessage(MessageDto messageDto) {
        if (messageDto.getUsername() == null || messageDto.getText() == null) {
            throw new InvalidMessageException("Username and message cannot be null");
        }
//        if (!userService.existsByUsername(messageDto.getUsername(), defaultChatRoom)) {
//            throw new UserNotFoundException(messageDto.getUsername());
//        }
    }


}
