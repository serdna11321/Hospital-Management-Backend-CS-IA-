package com.example.ia_cs.services;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ConnectionService {
    private final Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public void addConnection(WebSocketSession session, int userID){
        sessions.put(userID, session);
    }

    public void removeConnection(int userID){
        sessions.remove(userID);
    }

    public WebSocketSession getConnection(int userID){
        return sessions.get(userID);
    }

    public boolean isConnected(int userID){
        WebSocketSession session = getConnection(userID);
        return session!=null && session.isOpen();
    }
}

