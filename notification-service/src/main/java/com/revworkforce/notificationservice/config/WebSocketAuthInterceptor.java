package com.revworkforce.notificationservice.config;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
            String email = accessor.getFirstNativeHeader("X-User-Email");
            if (email != null && !email.isBlank()) {
                accessor.setUser(new SimplePrincipal(email));
            }
        }
        return message;
    }

    private record SimplePrincipal(String name) implements Principal {
        @Override
        public String getName() {
            return name;
        }
    }
}
