package co.com.poli.wsclient.client;

import co.com.poli.wsclient.model.MessageModel;
import co.com.poli.wsclient.model.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

@Slf4j
public class SessionHandler extends StompSessionHandlerAdapter {


    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        log.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/messages", this);
        log.info("Subscribed to /topic/messages");
        session.send("/app/chat", getSampleMessage());
        log.info("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        log.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return MessageModel.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        MessageResponse msg = (MessageResponse) payload;
        log.info("Received : " + msg.getText() + " from : " + msg.getFrom());
    }

    private MessageModel getSampleMessage() {
        MessageModel msg = new MessageModel();
        msg.setFrom("Juan!");
        msg.setText("Qué más!!");
        return msg;
    }
}