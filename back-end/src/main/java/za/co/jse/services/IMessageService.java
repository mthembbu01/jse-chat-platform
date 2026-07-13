package za.co.jse.services;

import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;

import java.util.List;

public interface IMessageService {
    MessageRespDto findByUsername(String username);

    List<ChatMessage> getDefaultChat();

    ChatMessage send(ChatMessage chatMessage) throws InterruptedException;
}
