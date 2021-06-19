package com.chat;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import javax.websocket.OnClose;

@ServerEndpoint("/chatroom/{roomName}")
public class ChatEndpoint {
	
	private static final Map<String, Set<Session>> rooms = new ConcurrentHashMap<String, Set<Session>>();
	 
	
	@OnOpen
    public void connect(@PathParam("roomName") String roomName, Session session) throws Exception {
        // Store the session according to the room name, and isolate users in each room
		System.out.println(" La sala es: " + roomName);
        if (!rooms.containsKey(roomName)) {
            // Create a room when the room does not exist
            Set<Session> room = new HashSet<>();
            // Add user
            room.add(session);
            rooms.put(roomName, room);
        } else {
            // The room already exists, add users directly to the corresponding room
            rooms.get(roomName).add(session);
        }
        System.out.println("a client has connected!");
    }
	
	@OnMessage
    public void receiveMsg(@PathParam("roomName") String roomName,
                           String msg, Session session) throws Exception {
        // There should be html filtering here
        System.out.println(msg + " La sala es: " + roomName);
        if(rooms.get(roomName)!=null) {
        	 System.out.println(msg + " La sesion existe!! es: " + rooms.get(roomName).toString());
        }
        // Broadcast after receiving the information
        broadcast(roomName, msg);
    }
	
	
	@OnClose
    public void disConnect(@PathParam("roomName") String roomName, Session session) {
        rooms.get(roomName).remove(session);
        System.out.println("a client has disconnected!");
    }
	
	/*@OnMessage
	public void message(String message, Session client) throws IOException, EncodeException {
		for (Session openSession : client.getOpenSessions()) {
			openSession.getBasicRemote().sendText(message);
		}
	}*/
	
	  public static void broadcast(String roomName, String msg) throws Exception {
	        for (Session session : rooms.get(roomName)) {
	                session.getBasicRemote().sendText(msg);
	        }
	  }

}