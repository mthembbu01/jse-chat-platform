package za.co.jse.services;

import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;

public interface IMessageService {
    MessageRespDto findByUsername(String username);

    void addMessage(MessageDto messageDto);
}
