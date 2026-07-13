package za.co.jse.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

/**
 * WebSocket Configuration for real-time chat messaging
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    
    /**
     * Register STOMP endpoints for WebSocket connections
     * @param registry
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //-- Register the endpoint
        registry.addEndpoint("/ws-chat") //Angular connects to this end point!
                .setAllowedOriginPatterns(
                    "http://localhost:4200", 
                    "http://localhost:4200/", 
                    "http://127.0.0.1:4200", 
                    "http://127.0.0.1:4200/",
                    "http://localhost:*",
                    "http://127.0.0.1:*"
                )
                .withSockJS()
                .setClientLibraryUrl("https://cdn.jsdelivr.net/npm/sockjs-client@1/dist/sockjs.min.js");
    }
    
    /**
     * Enable and configure the message broker
     * @param registry
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //-- Enable the message broker
        registry.enableSimpleBroker("/topic");

        //-- Set the prefix for the application destination (client sends to /app/*)
        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Configure WebSocket transport settings
     * @param registration
     */
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registration) {
        registration
            .setMessageSizeLimit(160 * 64 * 1024) // 10 MB
            .setSendTimeLimit(20 * 1000) // 20 seconds
            .setSendBufferSizeLimit(3 * 512 * 1024); // 1.5 MB
    }
}
