package za.co.jse.entities;

import jakarta.annotation.Resource;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Component;
import za.co.jse.entities.dtos.MessageDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ChatRoom {
    private String name;
    private Map<String, List<Message>> chat = new HashMap<>();

}
