package networking;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
	int maxAck = 0;
	int topSent = 0;
	int recoveryThreshold = 0;
	int numAck = 0;
	private State state = State.SLOW_START;
	int windowThreshold = -1;
	double timeoutTime = 500;
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
		g.setColor(new Color(0, 255, 0, 140));
		g.fillOval(source.x - 10, source.y - 10, 20, 20);
		
//		int arcWidth = Math.abs(dest.x - source.x);
//		int arcHeight = Math.abs(dest.y - source.y);
//		g.fillArc(source.x, source.y, arcWidth, arcHeight, 0, 180);
//		g.drawArc(100, 100, 100, 100, 180, 180);
		g.drawLine(source.x, source.y, dest.x, dest.y);
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
		NumberFormat fmt = new DecimalFormat("#0.0000");
/*		StreamManager.print("window", fmt.format(Network.currTime) + "\t" + 
										source.addr + "\t" + 
										window + "\n");*/
		
		if(p.id > maxPacket) {
			closeFlow(q);
			return;
		}
		
		if(p.id > topSent) {
			topSent = p.id - 1;
		}
		
		if(lastAck == -1) {
			lastAck = p.id - 1;
		}
		
		if(p.id > maxAck) {
			StreamManager.print("flow", fmt.format(Network.currTime) + "\t" + this.toString() + "\t" + (p.id - maxAck) + "\n");
			maxAck = p.id;
		}
		
//		windowSaturation -= p.id - lastAck;
		windowSaturation = topSent - p.id + 1;
		if(windowSaturation < 0) {
			windowSaturation = 0;
//			System.err.println("wndSat is negative");
//			System.exit(1);
		}
		
		setTimeout(q);
		
		switch(state) {
		case SLOW_START:
			window += p.id - lastAck;
			if(p.id == lastAck) {
				window++;
				numAck++;
			} else {
				numAck = 0;
			}
			
			// if we've received 3 acknowlegements for the same packet, retransmit
			if(numAck >= 3) {
				numAck = 0;
				
				// retransmit
				retransmit(lastAck, q);
				if(window > 1) {
					window /= 2;
					windowThreshold = window;
				}
//				timeout(q);
//				forgetSent(lastAck);
//				forgetSentPastId(lastAck);
//				windowThreshold = window / 2;
//				window = 1;
//				windowSaturation = 0;
			}
			
			if(window > windowThreshold && windowThreshold != -1) {
				changeState(State.COLLISION_AVOIDANCE);
				window = windowThreshold;
			}
			
			break;
		case FAST_RECOVERY:
/*//			retransmit(p.id, q);
			window++;
			topSent = maxAck - 1;
			if(p.id > recoveryThreshold) {
				window/=2;
				windowThreshold = window;
//				windowSaturation = window - 1;
				changeState(State.COLLISION_AVOIDANCE);
				break;
			}*/
			
			if(p.id == lastAck) {
				window++;
				numAck++;
			} else {
				numAck = 0;
				window/=2;
				windowThreshold = window;
//				windowSaturation = window - 1;
				changeState(State.COLLISION_AVOIDANCE);
				break;
			}
			
			// if we've received 3 acknowlegements for the same packet, retransmit
			if(numAck >= 3) {
				numAck = 0;
				retransmit(lastAck, q);
			}
			break;
		case COLLISION_AVOIDANCE:
			window++;
			if(p.id == lastAck) {
				numAck++;
			} else {
				numAck = 0;
			}
			
			// if we've received 3 acknowlegements for the same packet, retransmit
			if(numAck >= 3) {
				numAck = 0;
				retransmit(lastAck, q);
				recoveryThreshold = topSent;
				topSent = lastAck;
				changeState(State.FAST_RECOVERY);
			}
			break;
		}
		
		while(windowSaturation < window) {
			Packet newp = getPacket();
			if(newp != null) {
				source.request(newp, q);
//				windowSaturation++;
				windowSaturation = topSent - p.id + 1;
			} else {
				break;
			}
		}
		
		lastAck = p.id;
		Network.debug1 = topSent;
		Network.debug2 = lastAck;
		return;
	}
	
	private void closeFlow(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		for(Event e : q) {
			if(e.t == Event.Type.TIMEOUT && e.f == this) {
				q.remove(e);
				break;
			}
		}
	}

	private Packet getPacket(int id) {
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
/*		NumberFormat fmt = new DecimalFormat("#0.0000");
		StreamManager.print("window", fmt.format(Network.currTime) + "\t" + 
										source.addr + "\t" + 
										window + "\n");*/
		
//		System.err.println("Timeout");
//		StreamManager.print("packet", "Timeout\n");
		
		if(windowThreshold != -1) {
			windowThreshold /= 2;
		} else {
			windowThreshold = window / 2;
		}
		
		window = 1;
		windowSaturation = 0;
		topSent = lastAck - 1;
		changeState(State.SLOW_START);
		lastAck = -1;
		
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
				break;
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
	
	private void changeState(State s) {
		this.state = s;
		String message = "";
		switch(state) {
		case SLOW_START:
			message = "SLOW_START";
			break;
		case FAST_RECOVERY:
			message = "FAST_RECOVERY";
			break;
		case COLLISION_AVOIDANCE:
			message = "COLLISION_AVOIDANCE";
			break;
		}
//		StreamManager.print("packet", message + "\n");
	}
	
	@Override
	public String toString() {
		return source.addr + ":" + dest.addr;
	}
	
	public String exportString() {
		// TODO Auto-generated method stub
		String output = source.addr + " " + 
						dest.addr + " " + 
						packetSize + " " + 
						congestionType + " " + 
						dataAmt + " " + 
						(int) delay;
		return output;
	}

	public boolean entails(Host h) {
		// TODO Auto-generated method stub
		return source == h || dest == h;
	}
}
