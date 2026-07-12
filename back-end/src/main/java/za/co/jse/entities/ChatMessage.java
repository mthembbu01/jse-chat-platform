package za.co.jse.entities;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
public class ChatMessage {
    private String username;

    private String text;

    private LocalDateTime timestamp;
}