
package com.me.module.udp.client;

import com.me.module.udp.*;
import java.io.IOException;
import java.net.*;
import org.apache.log4j.Logger;

public class UDPClient implements Runnable{

	private Connection connection;
	private boolean running;
	private DatagramSocket socket;
	private Thread process;
	private Thread send;
	private Thread receive;
	private static final Logger LOG = Logger.getLogger(UDPClient.class);

	public UDPClient(String adddress, int port){
		try{
			socket = new DatagramSocket();
			connection = new Connection(socket, InetAddress.getByName(adddress), port);
			init();
		}catch(SocketException | UnknownHostException ex){
			LOG.error(ex.getMessage());
		}
	}

	private void init(){
		process = new Thread(this, "server_process");
		process.start();
	}

	public void send(byte[] data){
		send = new Thread("sendind_thread"){
			@Override
			public void run(){
				connection.send(data);
			}

		};
		send.start();
	}

	public void receive(PacketHandler handler){
		receive = new Thread("receive_thread"){
			@Override
			public void run(){
				while(running){
					byte[] buffer = new byte[1024];
					DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
					try{
						socket.receive(packet);
					}catch(IOException ex){
						LOG.error(ex.getMessage());
					}
					handler.process(new Packet(packet.getData(), packet.getAddress(), packet.getPort()));
				}
			}

		};
		receive.start();
	}

	public void close(){
		running = false;
		connection.close();
	}

	@Override
	public void run(){
		running = true;
	}

}
