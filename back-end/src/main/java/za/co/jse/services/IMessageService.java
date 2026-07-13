package za.co.jse.services;

import za.co.jse.entities.ChatMessage;
import za.co.jse.entities.ChatUser;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;

public interface IMessageService {
    MessageRespDto findByUsername(String username);

    ChatUser join(String username);

    ChatMessage send(MessageDto messageDto) throws InterruptedException;
}
