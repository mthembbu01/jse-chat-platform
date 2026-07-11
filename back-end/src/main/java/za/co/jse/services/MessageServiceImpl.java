package za.co.jse.services;


import za.co.jse.entities.Message;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class MessageServiceImpl implements IMessageService {

    private Map<String, List<Message>> chats = new HashMap<>();


    /**
     *
     * @param username
     * @return
     */
    @Override
    public MessageRespDto findByUsername(String username) {
        final List<Message> messages = chats.computeIfAbsent(username, k -> new ArrayList<>());
        return new MessageRespDto(username,messages);
    }

    public void addMessage(MessageDto messageDto) {
        validateMessage(messageDto);
        findByUsername(messageDto.getUsername()).getMessages().add(Message.builder().text(messageDto.getText()).timestamp(LocalDateTime.now()).build());
    }

    private static void validateMessage(MessageDto messageDto) {
        if (messageDto.getUsername() == null || messageDto.getText() == null) {
            throw new RuntimeException("Username and message cannot be null");
        }
    }


}
