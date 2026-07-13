package za.co.jse.entities.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    
    @JsonProperty("time")
    private String time;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    /**
     * Get the timestamp, using time if timestamp is not set
     */
    public LocalDateTime getTimestamp() {
        if (this.timestamp != null) {
            return this.timestamp;
        }
        if (this.time != null && !this.time.isEmpty()) {
            return LocalDateTime.now();
        }
        return LocalDateTime.now();
    }
}
