package org.socket.server;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.*;

@ServerEndpoint("/server")
public class SocketServer { 
	
	// Storing the sessions
	static Set <Session> clientUIs = Collections.synchronizedSet(new HashSet<Session>());
	@OnOpen
	public void handleOpened(Session clientSession) throws IOException {
		clientUIs.add(clientSession);
		
			clientSession.getBasicRemote().sendText(buildJsonData("System", "You are now connected to the Map Server!!!"));
	
		System.out.println("A New Client is Connected!!!!");
	}
	
	@OnMessage
	public void handleMessage(String message, Session clientSession) throws IOException{
			System.out.println("A Message Received!!!");
					
			Iterator<Session> iterator = clientUIs.iterator();
			while (iterator.hasNext()){
				
				iterator.next().getBasicRemote().sendText(buildJsonData("Message : ", message));
				
			}
	}
	
	@OnClose
	public void handleClose (Session id){
		
		clientUIs.remove(id);
	}

	private String buildJsonData(String id, String message) {
		// TODO Auto-generated method stub
		JsonObject json = Json.createObjectBuilder().add("message", id+ ": " +message).build();
		StringWriter strwriter = new StringWriter();
	
			try (JsonWriter jsonwriter = Json.createWriter(strwriter)) {jsonwriter.write(json);}
		
			System.out.println("The Jason : "+strwriter.toString());
		return strwriter.toString();
	}

}