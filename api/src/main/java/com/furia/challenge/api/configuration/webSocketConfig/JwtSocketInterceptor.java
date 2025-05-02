package com.furia.challenge.api.configuration.webSocketConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

@Component
public class JwtSocketInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtDecoder jwtDecoder;

    private final JwtAuthenticationConverter authenticationConverter = new JwtAuthenticationConverter();

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                try {
                    Jwt jwt = jwtDecoder.decode(token);
                    authenticationConverter.convert(jwt);
                } catch (JwtException e) {
                    throw new IllegalArgumentException("Token JWT inválido", e);
                }
            } else {
                throw new IllegalArgumentException("Authorization header ausente ou malformado");
            }
        }

        return message;
    }
}