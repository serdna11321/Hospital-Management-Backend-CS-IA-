package com.example.ia_cs.Handlers;

import com.example.ia_cs.services.ConnectionService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.security.Principal;

@Component
public class UpdateWebSocketHandler extends TextWebSocketHandler {

    private final ConnectionService connectionService;

    public UpdateWebSocketHandler(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("User connected");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        int userID = getUserID(session.getPrincipal());
        connectionService.addConnection(session, userID);
    }

    private int getUserID(Principal principal){
        if(principal != null){
            return Integer.parseInt(principal.getName());
        }

        throw new IllegalStateException("No authenticated user found");
    }
}