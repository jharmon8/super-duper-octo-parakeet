package networking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.PriorityQueue;

import networking.Event.Type;

public class Link {
	//test
	String addr1, addr2;
	int rate, latency;
	Device[] devices = new Device[2];
	
	// the buffer and corresponding sources of each packet
	// (to keep track of directionality)
	ArrayList<Packet> buffer = new ArrayList<Packet>();
	ArrayList<Device> sources = new ArrayList<Device>();
	
	// The end time that the last packet will finish transmitting (no delay)
	// Used when new packets are added to the buffer and need trans events
	private int bufferEndTime = 0;
	
	public Link(Device d1, Device d2, int rate, int latency) {
		this.devices[0] = d1;
		this.devices[1] = d2;
		this.rate = rate;
		this.latency = latency;
	}
	
	public Link(Device d1, Device d2, String rate, String latency) {
		this.devices[0] = d1;
		this.devices[1] = d2;
		this.rate = Integer.parseInt(rate);
		this.latency = Integer.parseInt(latency);
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
		int transTime = p.size / rate;

		// if this source is the same as the last packet
		if(source == sources.get(sources.size() - 1)) {
			// don't worry about delay
			bufferEndTime += transTime;
		} else {
			// gotta worry about delay
			bufferEndTime += transTime + latency;
		}
		
		// create the event
		Event e = new Event(
				Type.TRANS, 
				transTime + bufferEndTime, 
//				this, 
				this, 
				p
		);
		q.add(e);
		
		// add the packet and source to buffer
		buffer.add(p);
		sources.add(source);
	}
	
	// Send the top packet on the buffer on its merry way
	public void pop(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		Packet pack = buffer.remove(0);
		Device dest = sources.remove(0) == devices[0] ? devices[1] : devices[0];
		
		dest.route(pack, q);
	}
}
