package networking;

import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;

import io.StreamManager;

public abstract class Flow {
	Device source, dest;
	int packetSize, window, dataAmt, congestionType;
	double delay;
	int windowSaturation = 0;
	int lastAck = 1;
	int topSent = 0;
	int numAck = 0;
	int windowThreshold = -1;
	double timeoutTime = 500;
	int maxPacket;
	
	public enum State {
		SLOW_START, FAST_RECOVERY, COLLISION_AVOIDANCE
	}
	
	public Flow() {}
	
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	// Send first packet by putting trans event on q
	public abstract void init(PriorityQueue<Event> q);
	
	// Generate a packet for the source to send
	// Will be called every time the src has an opportunity to send a packet
	public Packet getPacket() {
		int nextId = getNextPacketId();
		if(nextId <= maxPacket) {
			Packet p = new Packet(packetSize, source, dest, this, nextId);
			if(nextId > topSent) {
				topSent = nextId;
			}
			return p;
		}
		
		return null;
	}

	protected void retransmit(int id, PriorityQueue<Event> q) {
		Packet newp = getPacket(lastAck);
		source.request(newp, q);
	}

	public abstract void acknowledge(Packet p, PriorityQueue<Event> q);
	
	protected void closeFlow(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		for(Event e : q) {
			if(e.t == Event.Type.TIMEOUT && e.f == this) {
				q.remove(e);
				break;
			}
		}
	}

	protected Packet getPacket(int id) {
		int nextId = id;
		if(nextId <= maxPacket) {
			Packet p = new Packet(packetSize, source, dest, this, nextId);
			if(nextId > topSent) {
//				topSent = nextId;
			}
			return p;
		}
		
		return null;
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
	
	protected int getNextPacketId() {
		return topSent + 1;
	}

	public void addStartEvent(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		q.add(new Event(Event.Type.START, delay, this));
	}
	
	public abstract void timeout(PriorityQueue<Event> q);
	
	protected void setTimeout(PriorityQueue<Event> q) {
		for(Event e : q) {
			if(e.t == Event.Type.TIMEOUT && e.f == this) {
				q.remove(e);
				break;
			}
		}
		
		Event e = new Event(Event.Type.TIMEOUT, Network.currTime + timeoutTime, this);
		q.add(e);
	}

	protected void forgetSentPastId(int id) {
		topSent = id;
	}
	
	@Override
	public String toString() {
		return source.addr + ":" + dest.addr;
	}
}
