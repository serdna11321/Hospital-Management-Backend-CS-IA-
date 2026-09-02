package com.example.ia_cs.Configuration;

import com.example.ia_cs.Handlers.UpdateWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocketSecurity
public class WebsocketConfiguration implements WebSocketConfigurer {

    private final UpdateWebSocketHandler handler;

    public WebsocketConfiguration(UpdateWebSocketHandler handler) {
        this.handler = handler;
    }

    @Override
    public void registerWebSocketHandlers(
            WebSocketHandlerRegistry registry) {

        registry.addHandler(handler, "/updates").setAllowedOrigins("*");
    }
}
