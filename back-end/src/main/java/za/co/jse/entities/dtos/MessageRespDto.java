package za.co.jse.entities.dtos;


import lombok.*;
import za.co.jse.entities.Message;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class MessageRespDto {
    private String username;
    private List<Message> messages;
}
