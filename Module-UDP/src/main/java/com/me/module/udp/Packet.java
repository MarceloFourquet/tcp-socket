
package com.me.module.udp;

import java.net.InetAddress;

public class Packet{

	private byte[] data;
	private InetAddress address;
	private int port;
	private Connection connection;

	public Packet(byte[] data, Connection connection){
		this.data = data;
		this.connection = connection;
	}

	public Packet(byte[] data, InetAddress address, int port){
		this.data = data;
		this.address = address;
		this.port = port;
		this.connection = new Connection(null, address, port);
	}

	public byte[] getData(){
		return data;
	}

	public InetAddress getAddress(){
		return address;
	}

	public int getPort(){
		return port;
	}

	public Connection getConnection(){
		return connection;
	}

	@Override
	public String toString(){
		return String.format("Data: %s%nFrom: %s:%s", new String(data), connection.getAddress(), connection.getPort());
	}

}
