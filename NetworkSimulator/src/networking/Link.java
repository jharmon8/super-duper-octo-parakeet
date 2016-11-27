package networking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.PriorityQueue;

import networking.Event.Type;

public class Link {
	//test
	String addr1, addr2;
	// rate in bps, latency in us, maxSize in KB
	int rate, latency, maxSize;
	Device[] devices = new Device[2];
	
	// the buffer and corresponding sources of each packet
	// (to keep track of directionality)
	ArrayList<Packet> buffer = new ArrayList<Packet>();
	ArrayList<Device> sources = new ArrayList<Device>();
	
	// The end time that the last packet will finish transmitting (no delay)
	// Used when new packets are added to the buffer and need trans events
	private double bufferEndTime = 0;
	
	public Link(Device d1, Device d2, int rate, int latency, int maxSize) {
		this.devices[0] = d1;
		this.devices[1] = d2;
		this.rate = rate;
		this.latency = latency;
		this.maxSize = maxSize;
	}
	
	public Link(Device d1, Device d2, String rate, String latency, String maxSize) {
		this.devices[0] = d1;
		this.devices[1] = d2;
		this.rate = Integer.parseInt(rate);
		this.latency = Integer.parseInt(latency);
		this.maxSize = Integer.parseInt(maxSize);
	}
	
	public boolean containsDevice(Device d) {
		return (devices[0] == d || devices[1] == d);
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLACK);
		g.drawLine(devices[0].x, devices[0].y, devices[1].x, devices[1].y);
		
		int midX = (devices[0].x + devices[1].x) / 2;
		int midY = (devices[0].y + devices[1].y) / 2;
		
		g.fillOval(midX - 2, midY - 2, 4, 4);
		
		int leftY = devices[0].x < devices[1].x ? devices[0].y : devices[1].y;
		int rightY = devices[0].x < devices[1].x ? devices[1].y : devices[0].y;
		
		if(leftY > rightY + 5) {
			g.drawString("Rate: " + rate, midX + 10, midY + 10);
			g.drawString("Latency: " + latency, midX + 10, midY + 20);
		} else {
			g.drawString("Rate: " + rate, midX + 10, midY - 25);
			g.drawString("Latency: " + latency, midX + 10, midY - 15);
		}
	}
	
	public void addPacket(Device source, Packet p, PriorityQueue<Event> q) {
		Device dest = source == devices[0] ? devices[1] : devices[0];
		double temp = buffer.isEmpty() ? Network.currTime : bufferEndTime;
		
		// maaaaaaan fuck that
//		if(!dest.isHost()) {
			int bufferSaturation = 0;
			for(Packet packet : buffer) {
				bufferSaturation += packet.size;
			}
			if(bufferSaturation + p.size > maxSize) { 
				System.err.println("packet dropped");
				return; 
			}
//		}
		
		double transTime = ((double)p.size / rate) * 1000;

		// if this source is the same as the last packet
		if(sources.size() == 0 || source != sources.get(sources.size() - 1)) {
			// gotta worry about delay
			temp += transTime + latency;
		} else {
			// don't worry about delay
			temp += transTime;
		}
		
		// create the events
/*		Event e1 = new Event(
				Type.OPPOR, 
				//TODO undo this 
				bufferEndTime + transTime, 
//				this, 
				this, 
				source,
				p
		);
		q.add(e1);*/
		Event e2 = new Event(
				Type.TRANS, 
				//TODO undo this 
				temp, 
//				this, 
				this, 
				source,
				p
		);
		q.add(e2);
		
		bufferEndTime = temp;
		
		// add the packet and source to buffer
		buffer.add(p);
		sources.add(source);
	}
	
	// Send the top packet on the buffer on its merry way
	public void pop(PriorityQueue<Event> q) {
		
		Packet pack = buffer.remove(0);
		Device dest = sources.remove(0) == devices[0] ? devices[1] : devices[0];
		
		dest.route(this, pack, q);
		
/*		if(buffer.isEmpty()) {
			Event e = new Event(
					Type.OPPOR,
					-1, // I think -1 will ensure the event happens next?
					this,
					dest,
					pack
			);
		}*/
	}
	
	// bypass all the shit
	// (this is why our bellman-ford is "cheating")
	public void send(Device src, String addr, int dist) {
		if(devices[0] == src) {
			devices[1].bfReceive(this, addr, dist);
		} else {
			devices[0].bfReceive(this, addr, dist);
		}
	}
}
