
package com.me.module.udp.client;

import com.me.module.udp.Packet;
import com.me.module.udp.PacketHandler;

public class RunnerUDPClient{

	private static UDPClient client;

	public static void main(String[] args){
		client = new UDPClient("localhost", 1337);
		client.receive(new PacketHandler(){
			@Override
			public void process(Packet packet){
				String data = new String(packet.getData()).trim();
				System.out.println(data.trim());
			}

		});
		client.send("CON".getBytes());
	}

}
