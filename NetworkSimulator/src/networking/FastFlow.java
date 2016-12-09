package networking;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.PriorityQueue;

import io.StreamManager;

public class FastFlow extends Flow {
//	private double lastAckTime = 0;
	private double baseRTT = -1;
	private double lastRTT = -1;
	private double gamma = .2;
	private int alpha = 128;
	private int currBatch = 0;
	
	private LinkedList<TimePair> times = new LinkedList<TimePair>();
	
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
		times.add(new TimePair(0, Network.currTime));
		while(windowSaturation < window) {
			Packet p = getPacket();
			if(p != null) {
				p.batch = 0;
				source.request(p, q);
			}
			windowSaturation++;
		}
	}

	@Override
	public void acknowledge(Packet p, PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		NumberFormat fmt = new DecimalFormat("#0.0000");
		if(p.id > maxAck) {
			StreamManager.print("flow", fmt.format(Network.currTime) + "\t" + this.toString() + "\t" + (p.id - maxAck) + "\n");
			maxAck = p.id;
		}		
		
		if(p.id > topSent) {
			topSent = p.id - 1;
		}
		
		if(p.id > maxPacket) {
			closeFlow(q);
			return;
		}
		
		setTimeout(q);
		
//		double rtt = Network.currTime - lastAckTime;
//		lastAckTime = Network.currTime;
		
		double rtt = lastRTT;
		for(TimePair tp : times) {
			if(tp.batch == p.batch) {
				rtt = Network.currTime - tp.time;
				lastRTT = rtt;
				times.remove(tp);
				break;
			}
		}
		
		topSent = p.id - 1;
		
		if((rtt < baseRTT || baseRTT == -1) && rtt != -1) {
			baseRTT = rtt * .9;
		}
		
		windowSaturation -= p.id - lastAck;
		window = Math.min(
					2*window, 
					(int)((1-gamma) * window + gamma * ((baseRTT / rtt) * window + alpha)));
		
		currBatch++;
		if(windowSaturation < window) {
			times.add(new TimePair(currBatch, Network.currTime));
		}
		
		while(windowSaturation < window) {
			Packet newp = getPacket();
			if(newp != null) {
				newp.batch = currBatch;
				source.request(newp, q);
				windowSaturation++;
			} else {
				break;
			}
		}
		
		lastAck = p.id;
//		Network.debug1 = (int)(rtt * 1000);
//		Network.debug2 = (int)(baseRTT * 1000);
		Network.debug1 = topSent;
		Network.debug2 = lastAck;
		Network.debug3 = this.toString();
	}

	@Override
	public void timeout(PriorityQueue<Event> q) {
		System.err.println("Timeout");
//		StreamManager.print("packet", "Timeout\n");

		window = window / 2;
		windowSaturation = 0;
		topSent = lastAck - 1;
		baseRTT = -1;
//		lastAckTime = Network.currTime;
		
		currBatch++;
		if(windowSaturation < window) {
			times.add(new TimePair(currBatch, Network.currTime));
		}
		
		while(windowSaturation < window) {
			Packet newp = getPacket(currBatch);
			if(newp != null) {
				newp.batch = currBatch;
				source.request(newp, q);
				windowSaturation++;
			} else {
				break;
			}
		}
		
		setTimeout(q);	
	}
	
	private class TimePair {
		int batch;
		double time;
		
		public TimePair(int batch, double time) {
			this.batch = batch;
			this.time = time;
		}
	}
}