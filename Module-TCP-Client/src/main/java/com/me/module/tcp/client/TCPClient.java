
package com.me.module.tcp.client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import org.apache.log4j.Logger;

public class TCPClient{

	private Socket clientSocket;
	private PrintWriter outputStream;
	private BufferedReader inputStream;
	private static final Logger LOG = Logger.getLogger(TCPClient.class);

	public static void main(String[] args) throws IOException{
		TCPClient client = new TCPClient();
		client.startConnection("127.0.0.1", 5555);
		Scanner scn = new Scanner(System.in);
		while(true){
			System.out.print("Enter a message: ");
			String mensaje = scn.nextLine();
			String returnedMessage = client.sendMessage(mensaje);
			if(mensaje.equals(".")){
				System.out.printf("Leaving the application - %s%n", returnedMessage);
				client.stopConnection();
				System.exit(0);
			}else{
				System.out.printf("Message sent -> %s%n", returnedMessage);
			}
		}
	}

	public void startConnection(String ip, int port){
		try{
			clientSocket = new Socket(ip, port);
			outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
			inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		}catch(IOException ex){
			LOG.error(ex.getMessage());
		}
	}

	public void stopConnection(){
		try{
			inputStream.close();
			outputStream.close();
			clientSocket.close();
		}catch(IOException ex){
			LOG.error(ex.getMessage());
		}
	}

	public String sendMessage(String msg){
		try{
			outputStream.println(msg);
			return inputStream.readLine();
		}catch(IOException ex){
			LOG.error(ex.getMessage());
			return null;
		}
	}

}
