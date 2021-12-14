package network;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.SQLException;

import base.SpectrogramDrawing;

public class ClientThread extends Thread {

    Socket client;
    protected Room room;
    
    public static String clientMessage = "";

    public ClientThread(Room room) throws IOException, SQLException, ClassNotFoundException, Exception {
    	this.room = room;
    }
    
    public void forceQuit() {
    	if(client != null)
    		if(!client.isClosed())
				try {
					client.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
    	Room.socketState = 0;
    }
    
    public void run() {
        while(!SpectrogramDrawing.forceQuit && Room.running) {
            try {
	       	        room.message("Connecting to " + room.address + " on port " + room.port);
	       	        client = new Socket(room.address, room.port);
	       	 
	       	        room.message("Just connected to " + client.getRemoteSocketAddress());
	       	 
	       	        Room.socketState = 2;
	       	     BufferedReader read = new BufferedReader(new InputStreamReader(client.getInputStream()));
	       	        while(!SpectrogramDrawing.forceQuit && Room.running) {
	       	        	String str = "";
	       	        	if((str = read.readLine()) != null){
	       					clientMessage = str+"\n";
	       				}
	       	        	sleep(30);
	       	        }
	       	 
	       	        DataOutputStream out = new DataOutputStream(client.getOutputStream());
	       	        out.writeUTF("Hello from " + client.getLocalSocketAddress());
	       		    out.writeUTF("client: hello to server");
	       		    
	       		    read.close();
	       		    client.close();
	       		    Room.socketState = 0;
           }catch(Exception ex) {
        	   Room.socketState = 0;
        	   room.message(ex.toString());
           }
       }
    }

}
