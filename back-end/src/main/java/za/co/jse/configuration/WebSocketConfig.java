package za.co.jse.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 *
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    /**
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //-- Register the endpoint
        registry.addEndpoint("/jse-chat")
                .setAllowedOriginPatterns("http://localhost:4200");
    }
    /**
     * 1. Enable the message broker
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //-- Enable the message broker
        registry.enableSimpleBroker("/default-chat-room");
        //-- Set the prefix for the application
        registry.setApplicationDestinationPrefixes("/app");
    }

}
