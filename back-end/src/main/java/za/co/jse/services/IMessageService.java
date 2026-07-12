package za.co.jse.services;

import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;

public interface IMessageService {
    MessageRespDto findByUsername(String username);

    ChatMessage send(MessageDto messageDto) throws InterruptedException;
}
