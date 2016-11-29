package networking;

import io.StreamManager;

import java.util.PriorityQueue;

import networking.RenoFlow.State;

public class FastFlow extends Flow {
	private double lastAckTime = 0;
	private double baseRTT = -1;
	private double gamma = .05;
	private int alpha = 60;
	
	public FastFlow(Device source, Device dest, String packetSize, String dataAmt, String delay) {
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

	@Override
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

	@Override
	public void acknowledge(Packet p, PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		double rtt = Network.currTime - lastAckTime;
		lastAckTime = Network.currTime;
		
		if(rtt < baseRTT || baseRTT == -1) {
			baseRTT = rtt;
		}
		
		windowSaturation -= p.id - lastAck;
		window = Math.min(
					2*window, 
					(int)((1-gamma) * window + gamma * ((baseRTT / rtt) * window + alpha)));
		
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
	}

	@Override
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
}
