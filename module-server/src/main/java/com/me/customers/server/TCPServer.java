
package com.me.customers.server;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import org.apache.log4j.Logger;
import com.me.customers.repository.CustomerDAO;

public class TCPServer{

	private ServerSocket serverSocket;
	private static final Logger LOG = Logger.getLogger(TCPServer.class);

	public static void main(String[] args){
		TCPServer server = new TCPServer();
		server.startConnection(5555);
	}

	public void startConnection(int port){
		try{
			serverSocket = new ServerSocket(port);
			while(true){
				LOG.info("Waiting a new message...");
				new ServerHandler(serverSocket.accept()).start();
			}
		}catch(IOException ex){
			LOG.error(ex.getMessage());
		}finally{
			stopConnection();
		}
	}

	public void stopConnection(){
		try{
			serverSocket.close();
		}catch(IOException ex){
			LOG.error(ex.getMessage());
		}
	}

	private static class ServerHandler extends Thread{

		private Socket clientSocket;
		private PrintWriter outputStream;
		private BufferedReader inputStream;

		public ServerHandler(Socket socket){
			this.clientSocket = socket;
		}

		@Override
		public void run(){
			try{
				outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
				inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				String inputLine;
				while((inputLine = inputStream.readLine()) != null){
					LOG.debug(String.format("Message received [%s] -> From [%s]",
					inputLine, clientSocket.getInetAddress().getHostName()));
					if(inputLine.equals(".")){
						outputStream.println("Bye");
						break;
					}
					outputStream.println(inputLine.toUpperCase());
					if(inputLine.length() > 0){
						saveCustomer(inputLine);
					}
				}
				inputStream.close();
				outputStream.close();
				clientSocket.close();
			}catch(IOException ex){
				LOG.error(ex.getMessage());
			}
		}

	}

	private static void saveCustomer(String inputLine){
		// inputLine -> #CUSTOMER:Raul Geomar;Peña Gomez;95024590;Corrientes 990;raul.pena@hotmail.com;+54911111111
		if(inputLine.contains("CUSTOMER:")){
			String[] customerData = inputLine.substring(inputLine.indexOf(":") + 1).split(";");
			if(customerData.length == 6){
				new CustomerDAO().createCustomer(customerData);
			}else{
				System.out.println("Incomplete data - The customer could not be created");
			}
		}else{
			System.out.println("Invalid message");
		}
	}

}
