
package com.me.module.udp;

import java.io.IOException;
import java.net.*;
import org.apache.log4j.Logger;

public class Connection{

	private InetAddress address;
	private int port;
	private DatagramSocket socket;
	private static final Logger LOG = Logger.getLogger(Connection.class);

	public Connection(DatagramSocket socket, InetAddress address, int port){
		this.address = address;
		this.port = port;
		this.socket = socket;
	}

	public void send(byte[] byteArray){
		DatagramPacket packet = new DatagramPacket(
		byteArray,
		byteArray.length,
		address,
		port);
		try{
			socket.send(packet);
		}catch(IOException ex){
			LOG.error(ex.getMessage());
		}
	}

	public byte[] receive(){
		byte[] byteArray = new byte[1024];
		DatagramPacket packet = new DatagramPacket(byteArray, byteArray.length);
		try{
			socket.receive(packet);
		}catch(IOException ex){
			LOG.error(ex.getMessage());
		}
		return packet.getData();
	}

	public int getPort(){
		return port;
	}

	public InetAddress getAddress(){
		return address;
	}

	public void close(){
		new Thread(){
			@Override
			@SuppressWarnings("SynchronizeOnNonFinalField")
			public void run(){
				synchronized(socket){
					socket.close();
				}
			}

		}.start();
	}

}
