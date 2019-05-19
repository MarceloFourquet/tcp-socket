
package com.me.module.udp.server;

import com.me.module.udp.*;
import org.apache.log4j.Logger;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

public class UDPServer implements Runnable{

	private int port;
	private DatagramSocket socket;
	private boolean running;
	private Thread send;
	private Thread receive;
	private Thread process;
	private static final Logger LOG = Logger.getLogger(UDPServer.class);
	public static ArrayList<Connection> clientes = new ArrayList<>();

	public UDPServer(int port){
		try{
			this.port = port;
			this.socket = new DatagramSocket(port);
			init();
		}catch(SocketException ex){
			LOG.error(ex.getMessage());
		}
	}

	private void init(){
		process = new Thread(this, "server_process");
		process.start();
	}

	public void send(Packet packet){
		send = new Thread("send_thread"){
			@Override
			public void run(){
				DatagramPacket datagramPacket = new DatagramPacket(
				packet.getData(),
				packet.getData().length,
				packet.getAddress(),
				packet.getPort()
				);
				try{
					socket.send(datagramPacket);
				}catch(IOException ex){
					LOG.error(ex.getMessage());
				}
			}

		};
		send.start();
	}

	public void broadcast(byte[] data){
		clientes.forEach((conn) -> {
			send(new Packet(data, conn.getAddress(), conn.getPort()));
		});
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

	@Override
	public void run(){
		running = true;
		System.out.printf("Server started on port %s%n", port);
	}

}
