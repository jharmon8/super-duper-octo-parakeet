package networking;

import java.awt.Graphics;
import java.util.PriorityQueue;

import networking.Event.Type;

public class Flow {
	Device source, dest;
	int packetSize, window, dataAmt, congestionType;
	double delay;
	int windowSaturation = 0;
	
	public Flow(Device source, Device dest, String packetSize, String congType, String dataAmt, String delay) {
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
		this.congestionType = Integer.parseInt(congType);
		this.dataAmt = Integer.parseInt(dataAmt);
		this.delay = Double.parseDouble(delay);
		this.window = 1;
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
	
	public void fastTCP()
	{
		double base = 100;
		double gam = .5;
		double RTT = 1;
		int alpha = 15;
		int temp1 = window * 2;
		double temp2 = (1 - gam)* window + gam * (window * (base / RTT) + alpha);
		Math.min(temp1, (int) temp2);
				}
}
