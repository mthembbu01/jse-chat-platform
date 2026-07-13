package za.co.jse.services;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.ChatRoom;
import za.co.jse.entities.dtos.MessageRespDto;
import za.co.jse.exceptions.InvalidMessageException;
import za.co.jse.exceptions.UserNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements IMessageService {

    @Getter
    private final ChatRoom defaultChatRoom;
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;

    /**
     *
     * @param username
     * @return
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

    @Override
    public List<ChatMessage> getDefaultChat() {
        return new ArrayList<>(defaultChatRoom.getChat());
    }

    public ChatMessage send(ChatMessage chatMessage) {
        //-- Validate the chatMessage
        validateMessage(chatMessage);
        publishToChatRoom(chatMessage);
        //--
        messagingTemplate.convertAndSend("/topic/messages", chatMessage);
        //--
        return chatMessage;
    }

    private void publishToChatRoom(ChatMessage chatMessage) {
        //--
        defaultChatRoom
                .getChat()
                .add(chatMessage);
    }

    private void validateMessage(ChatMessage chatMessage) {
        if (chatMessage.getUsername() == null || chatMessage.getText() == null) {
            throw new InvalidMessageException("Username and message cannot be null");
        }
        if (!userService.existsByUsername(chatMessage.getUsername(), defaultChatRoom)) {
            throw new UserNotFoundException(chatMessage.getUsername());
        }
    }


}
