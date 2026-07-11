package za.co.jse.entities.dtos;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class MessageDto {
    private String username;
    private String text;
    private LocalDateTime timestamp;

}
