package networking;

import java.awt.Graphics;
import java.util.PriorityQueue;

import networking.Event.Type;

public class Flow {
	Device source, dest;
	int packetSize, window;
	
	public Flow(Device source, Device dest, String packetSize, String window) {
		if(!source.isHost()) {
			System.err.println(source.addr + " is not a host");
			System.exit(1);
		}
		if(!dest.isHost()) {
			System.err.println(dest.addr + " is not a host");
			System.exit(1);
		}
		
		this.source = source;
		this.dest = dest;
		this.packetSize = Integer.parseInt(packetSize);
		this.window = Integer.parseInt(window);
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	// Send first packet by putting trans event on q
	public void init(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		source.route(getPacket(), q);
	}
	
	
	// Generate a packet for the source to send
	// Will be called every time the src has an opportunity to send a packet
	public Packet getPacket() {
		return new Packet(packetSize, source);
	}
}
