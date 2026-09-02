package com.example.ia_cs.services;

import com.example.ia_cs.dataTransferObjects.PeriodicUpdateDTOs.UpdateDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
public class UpdateSenderService {
    private final ConnectionService connectionService;
    private final ObjectMapper objectMapper;

    public UpdateSenderService(ConnectionService connectionService, ObjectMapper objectMapper) {
        this.connectionService = connectionService;
        this.objectMapper = objectMapper;
    }

    public void sendUpdate(int userID, UpdateDTO updateDTO){
        WebSocketSession session = connectionService.getConnection(userID);
        if (session == null || !session.isOpen()){
            return;
        }

        try {
            String json = objectMapper.writeValueAsString(updateDTO);

            session.sendMessage(new TextMessage(json));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
