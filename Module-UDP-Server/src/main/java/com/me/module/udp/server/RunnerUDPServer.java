
package com.me.module.udp.server;

import com.me.module.udp.Packet;
import com.me.module.udp.PacketHandler;
import com.me.module.udp.client.UDPClient;

public class RunnerUDPServer{

	private static UDPServer server;
	private static UDPClient client;

	public static void main(String[] args){
		server = new UDPServer(1337);
		client = new UDPClient("localhost", 1337);
		server.receive(new PacketHandler(){
			@Override
			public void process(Packet packet){
				String data = new String(packet.getData()).trim();
				if(data.equals("CON")){
					UDPServer.clientes.add(packet.getConnection());
					reply(new Packet("OK".getBytes(), packet.getAddress(), packet.getPort()));
				}
			}

		});
		client.receive(new PacketHandler(){
			@Override
			public void process(Packet packet){
				String data = new String(packet.getData()).trim();
				System.out.println(data.trim());
			}

		});
		client.send("CON".getBytes());
	}

	@SuppressWarnings("RedundantStringConstructorCall")
	public static void reply(Packet packet){
		server.broadcast(new String(packet.toString()).getBytes());
	}

}
