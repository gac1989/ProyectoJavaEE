package com.chat;

import java.io.IOException;
import javax.websocket.OnMessage;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import javax.websocket.EncodeException;

@ServerEndpoint("/chatroom")
public class ChatEndpoint {
	@OnMessage
	public void message(String message, Session client) throws IOException, EncodeException {
		for (Session openSession : client.getOpenSessions()) {
			openSession.getBasicRemote().sendText(message);
		}
	}
}