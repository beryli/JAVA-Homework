import java.io.*;
import java.net.*;
import java.util.concurrent.*;
import java.util.Random;

public class MultithreadingTCPServer {
	
	private String serverName = null;
	private int serverPort = 0;
	
	public MultithreadingTCPServer(String name, int port) {
		serverName = name;
		serverPort = port;
	}
	
	public void start(){
		
		//set server address
		InetSocketAddress serverSocketAddress = new InetSocketAddress(serverName, serverPort);		
		String localAddress = serverSocketAddress.getAddress().getHostAddress();
		
		//try-with-resources statement, following s will close serverSocket automatically
		try(ServerSocket serverSocket = new ServerSocket()) {
			
			//Binds the ServerSocket to a specific address
			System.out.println("Bind server socekt to " + localAddress + ":" + serverPort);
			serverSocket.bind(serverSocketAddress);
			System.out.println("Multithreading server binding success");			
			
			while(true) {
				//Accept new client's connection
				Socket socket = serverSocket.accept();
				//Create a thread to serve the client
				//Create a thread to execute ClientHandlingTask(socket)
				Thread thread = new Thread(new ClientHandlingTask(socket));
				thread.start();
			}
			
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("Server shutdown.");
		}
		
	}
	
	private class ClientHandlingTask implements Runnable {
		
		private Socket clientSocket = null;
		
		public ClientHandlingTask(Socket socket) {
			clientSocket = socket;
		}
		
		@Override
		public void run() {			
			//get the ip address and port number of the remote client
			InetSocketAddress clientSocketAddress = (InetSocketAddress)clientSocket.getRemoteSocketAddress();
			String clientAddress = clientSocketAddress.getAddress().getHostAddress();
			int clientPort = clientSocketAddress.getPort();
			String str = null;
			try{
				/* generate a random variable */
				Random r = new Random();
				int answer = r.nextInt(100)+1;
				
				//get input/output stream to the client
				InputStream inputStream = clientSocket.getInputStream();
				OutputStream outputStream = clientSocket.getOutputStream();				
				byte[] buf = new byte[1024];
				
				//length = 0 or -1 if inputStream has reached end-of-stream
				int length = inputStream.read(buf);
				while(length > 0) {
					str = new String(buf, 0, length);
					if(str.equals("new")) {
						answer = r.nextInt(100)+1;
						length = inputStream.read(buf);
						continue;
					}
					
					System.out.println(clientPort + " : " + answer);
					//int guessNumber = Integer.parseInt(str);
					try {
						int guessNumber = Integer.parseInt(str);
						if(guessNumber<1 || guessNumber>100) {
							str = String.valueOf(guessNumber) + " is not in the range";
						}else if(answer>guessNumber) {
							str = "Greater than " + String.valueOf(guessNumber);
						}else if(answer<guessNumber) {
							str = "Smaller than " + String.valueOf(guessNumber);
						}else {str = "bingo";}
					} catch (NumberFormatException e) {
						str = "invalid input";
					}
					//echo the message
					outputStream.write(str.getBytes());
					outputStream.flush();
					length = inputStream.read(buf);
				}
				
			} catch(IOException e1) {
				//e1.printStackTrace();
			} finally {
				//always close the resources before left
				try{
					clientSocket.close();
				} catch(IOException e2){}
				System.out.println("Disconnecting to " + clientAddress + ":" + clientPort);
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
		
		MultithreadingTCPServer server = new MultithreadingTCPServer(serverName, serverPort);
		server.start();
		
	}
	
}