package network;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Enumeration;

import base.SpectrogramDrawing;

public class ServerTest extends Thread {
	private ServerSocket serverSocket;
    Socket[] clients;
    Room room;
    protected int clientNum = 0;
    public static int sending = 0;

    public ServerTest(Room room, int socketNum) throws IOException, SQLException, ClassNotFoundException, Exception {
    	serverSocket = new ServerSocket(room.port);
    	this.room = room;
    	this.clientNum = socketNum;
    	//If nobody connects to the room for two minutes, shut the socket down.
    	serverSocket.setSoTimeout(120000);
    	//System.out.println(getLocalIpAddress());
    }
    
    public String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }
    
    public String getInfo() {
    	return serverSocket.getLocalSocketAddress() + ":" + serverSocket.getLocalPort();
    }
    
    public String getRemoteInfo() {
    	return getLocalIpAddress() + ":" + serverSocket.getLocalPort();
    }
    
    public void forceQuit() {
    	if(clients != null)
    		for(Socket c : clients) {
	    		if(c != null)
	    			if(!c.isClosed())
						try {
							c.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
    		}
    	Room.socketState = 0;
    }

    public void run() {
        while(!SpectrogramDrawing.forceQuit && Room.running) {
            try {
            	clients = new Socket[clientNum];
            	for(int i=0; i<clientNum; i++) {
            		room.message("Waiting for client #" +i+ " on "+room.address);
	            	clients[i] = serverSocket.accept();
            	}
               
               Room.socketState = 1;
               room.message("Server started.");
               PrintStream[] ps = new PrintStream[clientNum];
               for(int i=0; i<clientNum; i++) {
           			ps[i] = new PrintStream(clients[i].getOutputStream());
           		}
               
               while(!SpectrogramDrawing.forceQuit && Room.running) {
        			for(PrintStream s : ps) {
        				s.println(sending + '\n');
        			}
        			sleep(30);
   	        	}
               Room.socketState = 0;
           }catch(Exception ex) {
        	   Room.socketState = 0;
        	   room.message(ex.toString());
           }
       }
    }
}
