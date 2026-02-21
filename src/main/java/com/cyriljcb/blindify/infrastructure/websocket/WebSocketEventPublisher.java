package com.cyriljcb.blindify.infrastructure.websocket;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.cyriljcb.blindify.infrastructure.web.dto.BlindtestFinishedEvent;
import com.cyriljcb.blindify.infrastructure.web.dto.BlindtestStartedEvent;
import com.cyriljcb.blindify.infrastructure.web.dto.PhaseEvent;

@Service
public class WebSocketEventPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketEventPublisher(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void publishPhaseChange(PhaseEvent event) {
        System.out.println("🔴 [WebSocket] Publishing phase change: " + event);
        messagingTemplate.convertAndSend("/topic/blindtest/phase", event);
    }

    public void publishBlindtestStarted(BlindtestStartedEvent event) {
        System.out.println("🔴 [WebSocket] Publishing blindtest started: " + event);
        messagingTemplate.convertAndSend("/topic/blindtest/started", event);
    }

    public void publishBlindtestFinished(BlindtestFinishedEvent event) {
        System.out.println("🔴 [WebSocket] Publishing blindtest finished: " + event);
        messagingTemplate.convertAndSend("/topic/blindtest/finished", event);
    }
}