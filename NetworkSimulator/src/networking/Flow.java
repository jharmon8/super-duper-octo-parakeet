package networking;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.PriorityQueue;

import io.StreamManager;

public class Flow {
	Device source, dest;
	int packetSize, window, dataAmt, congestionType;
	double delay;
	int windowSaturation = 0;
//	ArrayList<Packet> sent = new ArrayList<Packet>();
	int lastAck = 1;
	int topSent = 0;
	int numAck = 0;
	private State state = State.SLOW_START;
	int windowThreshold = -1;
	double timeoutTime = 1000;
	int maxPacket;
	
	public enum State {
		SLOW_START, FAST_RECOVERY, COLLISION_AVOIDANCE
	}
	
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
		
		maxPacket = this.dataAmt / this.packetSize;
		if(maxPacket * this.packetSize < this.dataAmt) {
			maxPacket++;
		}
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
	
	// Send first packet by putting trans event on q
	public void init(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		while(windowSaturation < window) {
			Packet p = getPacket();
			if(p != null) {
				source.request(p, q);
			}
			windowSaturation++;
		}
	}
	
	
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
	
	// Host asks flow to send a packet
	// returns true if a packet is sent
	@Deprecated
	public boolean opportunity(Device d, PriorityQueue<Event> q) {
		Packet p = null;
		
		if(dataAmt > 0 && windowSaturation < window) {
			dataAmt -= packetSize;
			p = new Packet(packetSize, source, dest, this, getNextPacketId());
		}
		
		if(p != null) {
			d.request(p, q);
//			sent.add(p);
			windowSaturation++;
			return true;
		}
		
		return false;
	}
	
	private void retransmit(int id, PriorityQueue<Event> q) {
		Packet newp = getPacket(lastAck);
		source.request(newp, q);
	}

	public void acknowledge(Packet p, PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		if(p.id > maxPacket) {
			closeFlow(q);
			return;
		}
		
		setTimeout(q);
		
		switch(state) {
		case SLOW_START:
			windowSaturation -= p.id - lastAck;
			if(windowSaturation < 0) {windowSaturation = 0;}
			
			if(p.id == lastAck) {
				window++;
				numAck++;
			} else {
				numAck = 0;
				window += p.id - lastAck;
			}
			
			// if we've received 3 acknowlegements for the same packet, retransmit
			if(numAck >= 3) {
				numAck = 0;
				
				// retransmit
				retransmit(lastAck, q);
//				forgetSent(lastAck);
//				forgetSentPastId(lastAck);
//				windowThreshold = window / 2;
//				window = 1;
//				windowSaturation = 0;
			}
			
			if(window > windowThreshold && windowThreshold != -1) {
				state = State.COLLISION_AVOIDANCE;
			}
				
			break;
		case FAST_RECOVERY:
			windowSaturation -= p.id - lastAck;
			if(windowSaturation < 0) {windowSaturation = 0;}
			
			if(p.id == lastAck) {
				window++;
				numAck++;
			} else {
				numAck = 0;
				window/=2;
//				windowSaturation = window - 1;
				state = State.COLLISION_AVOIDANCE;
				break;
			}
			
			// if we've received 3 acknowlegements for the same packet, retransmit
			if(numAck >= 3) {
				numAck = 0;
				retransmit(lastAck, q);
			}
			break;
		case COLLISION_AVOIDANCE:
			windowSaturation -= p.id - lastAck;
			if(windowSaturation < 0) {windowSaturation = 0;}
			
			if(p.id == lastAck) {
				numAck++;
			} else {
				numAck = 0;
				window++;
			}
			
			// if we've received 3 acknowlegements for the same packet, retransmit
			if(numAck >= 3) {
				numAck = 0;
				retransmit(lastAck, q);
				state = State.FAST_RECOVERY;
			}
			break;
		}
		
		while(windowSaturation < window) {
			Packet newp = getPacket();
			if(newp != null) {
				source.request(newp, q);
				windowSaturation++;
			} else {
				break;
			}
		}
		
		lastAck = p.id;
		return;
	}
	
	private void closeFlow(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		for(Event e : q) {
			if(e.t == Event.Type.TIMEOUT && e.f == this) {
				q.remove(e);
			}
		}
	}

	private Packet getPacket(int id) {
		int nextId = id;
		if(nextId <= maxPacket) {
			Packet p = new Packet(packetSize, source, dest, this, nextId);
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
	
	private int getNextPacketId() {
/*		int i = 1;
		
		while(true) {
			boolean found = false;
			for(Packet p : sent) {
				if(p.id == i) {
					i++;
					found = true;
					break;
				}
			}
			
			if(!found) {
				break;
			}
		}
		
		return i;*/
		
		return topSent + 1;
	}

	public void addStartEvent(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		q.add(new Event(Event.Type.START, delay, this));
	}
	
	private void droppedPacket(int id, PriorityQueue<Event> q) {
//		window = window / 2;
//		for(Packet p : sent) {
//		}
	}
	
	public void timeout(PriorityQueue<Event> q) {
		System.err.println("Timeout");
		StreamManager.print("packet", "Timeout\n");
		
		if(windowThreshold != -1) {
			windowThreshold /= 2;
		} else {
			windowThreshold = window / 2;
		}
		
		window = 1;
		windowSaturation = 0;
		topSent = lastAck - 1;
		state = State.SLOW_START;
		
		while(windowSaturation < window) {
			Packet newp = getPacket();
			if(newp != null) {
				source.request(newp, q);
				windowSaturation++;
			} else {
				break;
			}
		}
		
		setTimeout(q);
	}
	
	private void setTimeout(PriorityQueue<Event> q) {
		for(Event e : q) {
			if(e.t == Event.Type.TIMEOUT && e.f == this) {
				q.remove(e);
			}
		}
		
		Event e = new Event(Event.Type.TIMEOUT, Network.currTime + timeoutTime, this);
		q.add(e);
	}
	
/*	private void forgetSent(int id) {
		for(Packet p : sent) {
			if(p.id == id) {
				sent.remove(p);
				return;
			}
		}
	}*/
	
	private void forgetSentPastId(int id) {
/*		ArrayList<Packet> toRemove = new ArrayList<Packet>();
		
		for(Packet p : sent) {
			if(p.id > id) {
				toRemove.add(p);
			}
		}
		
		for(Packet p : toRemove) {
			sent.remove(p);
		}*/
		
		topSent = id;
	}
}
