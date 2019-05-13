
package com.me.customers.server;

import java.io.*;
import java.net.Socket;
import java.net.ServerSocket;
import org.apache.log4j.Logger;
import com.me.customers.repository.CustomerDAO;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class TCPServer{

	private ServerSocket serverSocket;
	private static final Logger LOG = Logger.getLogger(TCPServer.class);
	private static final Pattern INPUT_LINE_PATTERN = Pattern.compile("^#CUSTOMER:[A-Za-z ñÑáéíóúÁÉÍÓÚüÜ]+;[A-Za-z ñÑáéíóúÁÉÍÓÚüÜ]+;[0-9]{7,10};[A-Za-z0-9º. ñÑáéíóúÁÉÍÓÚüÜ]+;[A-Za-z0-9-_]+(\\.[A-Za-z0-9-_]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*\\.[A-Za-z]{2,};\\+[0-9]{9,16}$");
	
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
						getCustomerData(inputLine);
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

	private static void getCustomerData(String inputLine){
		// inputLine -> #CUSTOMER:Victor Alberto;Montenegro Ortiz;99666777;Buena Vista 2646;victor.montenegro@mail.com;+54911911911
		Matcher m = INPUT_LINE_PATTERN.matcher(inputLine);
		if(m.matches()){
			String[] customerData = inputLine.substring(inputLine.indexOf(":") + 1).split(";");
			new CustomerDAO().createCustomer(customerData);
		}else{
			System.out.println("Invalid message");
		}
	}

}
