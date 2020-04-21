import java.io.*;
import java.net.*;
import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.*;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;


public class MultithreadingTCPClient extends JFrame {
	
	InputStream inputStream;
	OutputStream outputStream;
	SocketAddress severSocketAddress;
	Socket clientSocket;
	private String serverName = null;
	private int serverPort = 0;
	private JLabel displayText2;
	private JLabel displayText3;
	//GUI
	String input = "";
	private JPanel bottomJPanel;
	private JPanel buttonJPanel; // panel to hold buttons
	private JLabel displayText1;
	private JTextField inputText;
	private JLabel displayText;
	private JButton buttons;
    
	public MultithreadingTCPClient(String name, int port) {
        super("TCP");
		serverName = name;
		serverPort = port;
		

        buttonJPanel = new JPanel();
        buttonJPanel.setLayout(new GridLayout(4, 1));
        bottomJPanel = new JPanel();
        bottomJPanel.setLayout(new GridLayout(1, 2));
        
        displayText2 = new JLabel("", SwingConstants.CENTER);
        displayText3 = new JLabel("", SwingConstants.CENTER);
        // create
        displayText1 = new JLabel("Input an integer between 1 to 100: ", SwingConstants.CENTER);
        inputText = new JTextField("", 30);
        displayText = new JLabel("", SwingConstants.CENTER);
        buttons = new JButton("Submit");         
        //add to panel
        bottomJPanel.add(displayText2);
        bottomJPanel.add(displayText3);
        buttonJPanel.add(displayText1);
        buttonJPanel.add(inputText);
        buttonJPanel.add(displayText);
        buttonJPanel.add(buttons); // add button to panel
        buttons.addActionListener(new ActionHandler());

        add(buttonJPanel, BorderLayout.CENTER); // add panel to JFrame
        add(bottomJPanel, BorderLayout.SOUTH);
        buttonJPanel.setBackground(Color.WHITE);
	}
	
	class ActionHandler implements ActionListener
	{
	    @Override
	    public void actionPerformed(ActionEvent event)
	    {
			try {
				inputStream = clientSocket.getInputStream();
				outputStream = clientSocket.getOutputStream();
				//create a thread to read server's output(which is client's input)
				Thread task = new Thread(new ListeningTask(inputStream));
				task.start();
				
				//read keyboard message into a buf and write the message to the server
				byte[] buf = new byte[1024];
				String modifiedMessage = inputText.getText();
		    	buf = modifiedMessage.getBytes();
		    	int length = buf.length;
				//int length = System.in.read(buf);
				//length = -1 if there is no more data
				outputStream.write(buf, 0, length);
				outputStream.flush();
			} catch(IOException e) {
				e.printStackTrace();
			}
	    }
	}
	
	public void start() {
		//set server address
		SocketAddress severSocketAddress = new InetSocketAddress(serverName, serverPort);
		
		//try(Socket clientSocket = new Socket()) {
		try{
			clientSocket = new Socket();
			//connect to server in the specific timeout 3000 ms
			//System.out.println("Connect to server " + serverName + ":" + serverPort);
			clientSocket.connect(severSocketAddress, 3000);
			
			//get client address and port at local host
			InetSocketAddress socketAddress = (InetSocketAddress)clientSocket.getLocalSocketAddress();
			String clientAddress = socketAddress.getAddress().getHostAddress();
			int clientPort = socketAddress.getPort();
			//System.out.println("Client " + clientAddress + ":" + clientPort);
			//System.out.println("Connecting to server " + serverName + ":" + serverPort);
			displayText2.setText("Client: " + clientAddress + ":" + clientPort);
			displayText3.setText("Server: " + serverName + ":" + serverPort);
			
		} catch(IOException e1) {
			JOptionPane.showMessageDialog(null, "Service is currently disabled.");
			System.exit(1);
			//e1.printStackTrace();
		} //finally {
			//System.out.println("Connection shutdown");
		//}
	}
	
	private class ListeningTask implements Runnable {
		private InputStream inputStream = null;
		
		public ListeningTask(InputStream in) {
			inputStream = in;
		}
		
		@Override
		public void run() {
			//read server's output(which is client's input) into buf and write it to System
			byte[] buf = new byte[1024];			
			try {
				int length = inputStream.read(buf);
				while(length > 0) {
					String message = new String(buf, 0, length);
					displayText.setText("Return : " + message);
					//System.out.write(buf, 0, length);
					if(message.equals("bingo")) {
						JOptionPane.showMessageDialog(null, "Bingo!");
						if(JOptionPane.showConfirmDialog(null,
							"Continue the game?", "Congratulations!", JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) {
							byte[] arr = new byte[1024];
							String newGame ="new";
					    	arr = newGame.getBytes();
							outputStream.write(arr, 0, arr.length);
							outputStream.flush();
						}else {System.exit(1);}
					}
					length = inputStream.read(buf);
				}				
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
    
	public static void main(String[] args) {
		//Default server address is 127.0.0.1:12000		
		String serverName = "127.0.0.1";
		int serverPort = 12000;
		
		if(args.length >= 2) {
			serverName = args[0];
			try {
				serverPort = Integer.parseInt(args[1]);
			} catch(NumberFormatException e) {} 
		}
		
		MultithreadingTCPClient panelFrame = new MultithreadingTCPClient(serverName, serverPort);
		
	    panelFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    panelFrame.setSize(320, 200); 
	    panelFrame.setResizable(false);
	    panelFrame.setVisible(true);
	    panelFrame.setLocationRelativeTo(null);
	    panelFrame.start();
	}
}
