package networking;

import java.awt.Graphics;
import java.util.PriorityQueue;

import networking.Event.Type;

public class Flow {
	Device source, dest;
	int packetSize, window, dataAmt;
	double delay;
	int windowSaturation = 0;
	
	public Flow(Device source, Device dest, String packetSize, String window, String dataAmt, String delay) {
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
		this.dataAmt = Integer.parseInt(dataAmt);
		this.delay = Double.parseDouble(delay);
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	// Send first packet by putting trans event on q
	public void init(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
//		while(windowSaturation < window) {
			Packet p = getPacket();
			if(p != null) {
				source.request(p, q);
			}
			windowSaturation++;
//		}
	}
	
	
	// Generate a packet for the source to send
	// Will be called every time the src has an opportunity to send a packet
	public Packet getPacket() {
		if(dataAmt > 0) {
			dataAmt -= packetSize;
			return new Packet(packetSize, source, this);
		}
		
		return null;
	}
	
	// Host asks flow to send a packet
	// returns true if a packet is sent
	public boolean opportunity(Device d, PriorityQueue<Event> q) {
		Packet p = null;
		
		if(dataAmt > 0 && windowSaturation < window) {
			dataAmt -= packetSize;
			p = new Packet(packetSize, source, this);
		}
		
		if(p != null) {
			d.request(p, q);
			windowSaturation++;
			return true;
		}
		
		return false;
	}

	public void acknowledge(Packet p, PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		windowSaturation--;
		Packet newp = getPacket();
		if(newp != null) {
			source.request(newp, q);
		}
		windowSaturation++;
	}
}
