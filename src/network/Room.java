package network;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import scenes.FileSelectScene;

public class Room {
	public static int socketState = 0;

	public int port;
	public String address;
	
    byte[] bytes;
    
    private boolean isHost;
    private ServerTest hostThread;
    private ClientThread clientThread;
    
    private JTextField addressField, portField;
    private JFrame window;
	private JLabel hint;
	public static boolean running = false;
    
    public Room() {
		this.port = 0;
		this.address = "0.0.0.0";
		this.isHost = false;
		showSettingWindow();
		running = true;
    }
    
    public void restart() {
    	this.port = 0;
		this.address = "0.0.0.0";
		this.isHost = false;
		showSettingWindow();
		running = true;
    }
    
    public void forceQuit() {
    	running = false;
		if(clientThread != null) {
			clientThread.forceQuit();
			clientThread.interrupt();
		}
		if(hostThread != null) {
			hostThread.forceQuit();
			hostThread.interrupt();
		}
		Room.socketState = 0;
    }
    
    public void requestFocus() {
    	if(this.window != null) {
    		this.window.requestFocus();
    		this.window.setLocationRelativeTo(null);
    	}
    }
    
    public void sendPlayMessageToClients() {
    	if(this.isHost && hostThread != null) {
    		ServerTest.sending = 1;
    	}
    }
    
    public void sendStopMessageToClients() {
    	if(this.isHost && hostThread != null) {
    		ServerTest.sending = 2;
    	}
    }
	
    public void startAsHost(int port, int clientNum) throws Exception {
    	hostThread = new ServerTest(this, clientNum);
		window = new JFrame("Sender");
		window.setSize(640, 480);
    	window.setLocationRelativeTo(null);
    	window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	this.address = hostThread.getRemoteInfo();
    	hint = new JLabel("Server starting on "+address, JLabel.CENTER);
    	hint.setFont(new Font("Times New Roman", Font.BOLD, 24));
    	window.add(hint);
    	window.addWindowListener(new java.awt.event.WindowListener() {
			@Override
			public void windowOpened(java.awt.event.WindowEvent e) {}
			@Override
			public void windowIconified(java.awt.event.WindowEvent e) {}
			@Override
			public void windowDeiconified(java.awt.event.WindowEvent e) {}
			@Override
			public void windowDeactivated(java.awt.event.WindowEvent e) {}
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				forceQuit();
			}
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {}
			@Override
			public void windowActivated(java.awt.event.WindowEvent e) {}
		});
    	window.setVisible(true);
    	this.port = port;
    	this.isHost = true;
        hostThread.start();
    }
    
    public void message(String content) {
    	if(hint != null)
    		hint.setText(content);
    }
 
    public void startAsClient(String addr, int port) throws Exception {
    	clientThread = new ClientThread(this);
    	window = new JFrame("Sender");
    	window.setSize(640, 480);
    	window.setLocationRelativeTo(null);
    	window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	hint = new JLabel("Linking to LAN...", JLabel.CENTER);
    	hint.setFont(new Font("Times New Roman", Font.BOLD, 24));
    	window.add(hint);
    	window.addWindowListener(new java.awt.event.WindowListener() {
			@Override
			public void windowOpened(java.awt.event.WindowEvent e) {}
			@Override
			public void windowIconified(java.awt.event.WindowEvent e) {}
			@Override
			public void windowDeiconified(java.awt.event.WindowEvent e) {}
			@Override
			public void windowDeactivated(java.awt.event.WindowEvent e) {}
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				forceQuit();
			}
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {}
			@Override
			public void windowActivated(java.awt.event.WindowEvent e) {}
		});
    	window.setVisible(true);
    	this.port = port;
    	this.address = addr;
    	this.isHost = false;
    	clientThread.start();
   }
    
    public void showSettingWindow() {
		window = new JFrame("Set LAN");
		window.setSize(600, 400);
		window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		window.setLocationRelativeTo(null);
		window.setLayout(new GridLayout(5,1));
		window.addWindowListener(new java.awt.event.WindowListener() {
			@Override
			public void windowOpened(java.awt.event.WindowEvent e) {}
			@Override
			public void windowIconified(java.awt.event.WindowEvent e) {}
			@Override
			public void windowDeiconified(java.awt.event.WindowEvent e) {}
			@Override
			public void windowDeactivated(java.awt.event.WindowEvent e) {}
			@Override
			public void windowClosing(java.awt.event.WindowEvent e) {
				forceQuit();
			}
			@Override
			public void windowClosed(java.awt.event.WindowEvent e) {}
			@Override
			public void windowActivated(java.awt.event.WindowEvent e) {}
		});
		
		//Add an empty row in the JFrame
		window.add(new JLabel());
		
		JPanel inputPanel = new JPanel();
		inputPanel.setLayout(new GridLayout(3,2));
		inputPanel.add(new JLabel("IP Address / Client Number",JLabel.CENTER));
		addressField = new JTextField();
		addressField.setText("(IP address for clients & client number for a host)");
		inputPanel.add(addressField);
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel());
		inputPanel.add(new JLabel("Port",JLabel.CENTER));
		portField = new JTextField();
		inputPanel.add(portField);
		window.add(inputPanel);
		
		//Empty row
		window.add(new JLabel());
		
		//Add a label for user hints
		hint = new JLabel("Please enter username and password",JLabel.CENTER);
		window.add(hint);
		
		//Add button to confirm login or enter register window
		JPanel logInButtons = new JPanel();
		JButton logInButton = new JButton("Run as Host");
		logInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					window.dispose();
					int port = Integer.parseInt(portField.getText());
					int client = Integer.parseInt(addressField.getText());
					startAsHost(port, client);
				} catch (Exception e2) {
					e2.printStackTrace();
					FileSelectScene.guideline.setColor(180, 0, 0);
					FileSelectScene.guideline.update("Failed to start server.");
					window.dispose();
					forceQuit();
				}
			}
		});
		JButton registerButton = new JButton("Run as Client");
		registerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				window.dispose();
				try {
					String address = addressField.getText();
					int port = Integer.parseInt(portField.getText());
					startAsClient(address, port);
				} catch (Exception e1) {
					e1.printStackTrace();
					FileSelectScene.guideline.setColor(180, 0, 0);
					FileSelectScene.guideline.update("Failed connecting to server.");
					window.dispose();
					forceQuit();
				}
			}
		});
		logInButtons.add(logInButton);
		logInButtons.add(registerButton);
		window.add(logInButtons);
		window.setVisible(true);
	}

}
