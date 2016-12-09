package networking;

import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;

import io.StreamManager;

public class RenoFlow extends Flow {
	private State state = State.SLOW_START;
	
	public enum State {
		SLOW_START, FAST_RECOVERY, COLLISION_AVOIDANCE
	}
	
	public RenoFlow(Device source, Device dest, String packetSize, String dataAmt, String delay) {
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
		this.dataAmt = Integer.parseInt(dataAmt);
		this.delay = Double.parseDouble(delay);
		this.window = 1;
		
		maxPacket = this.dataAmt / this.packetSize;
		if(maxPacket * this.packetSize < this.dataAmt) {
			maxPacket++;
		}
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

	public void acknowledge(Packet p, PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
/*		NumberFormat fmt = new DecimalFormat("#0.0000");
		StreamManager.print("window", fmt.format(Network.currTime) + "\t" + 
										source.addr + "\t" + 
										window + "\n");*/
		
		NumberFormat fmt = new DecimalFormat("#0.0000");
		if(p.id > maxAck) {
			StreamManager.print("flow", fmt.format(Network.currTime) + "\t" + this.toString() + "\t" + (p.id - maxAck) + "\n");
			maxAck = p.id;
		}		

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
		
		windowSaturation -= p.id - lastAck;
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
				topSent = lastAck;
				changeState(State.FAST_RECOVERY);
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
		Network.debug1 = topSent;
		Network.debug2 = lastAck;
		return;
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
}