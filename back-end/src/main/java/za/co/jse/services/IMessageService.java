package za.co.jse.services;

import za.co.jse.entities.Message;
import za.co.jse.entities.dtos.MessageDto;
import za.co.jse.entities.dtos.MessageRespDto;

public interface IMessageService {
    MessageRespDto findByUsername(String username);

    Message addMessage(MessageDto messageDto);
}
