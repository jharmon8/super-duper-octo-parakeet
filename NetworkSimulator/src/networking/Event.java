package networking;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.PriorityQueue;
import java.util.Random;

import io.StreamManager;

public class Event implements Comparable<Event> {
	public enum Type {
		BFORD, START, OPPOR, TRANS, /*DELAY,*/ COMPL, TIMEOUT
	}
	
	public Type t;
	public double endTime;
	public Flow f;
	public Link l;
	public Packet p;
	public Device d;
	public Network n;
	private static Random rand = new Random(1234567);
	
	
	public Event(Type t, double endTime, Link l, Device d, Packet p) {
		// TODO Auto-generated constructor stub
		this.t = t;
		this.endTime = endTime;
//		this.f = f;
		this.l = l;
		this.d = d;
		this.p = p;
	}
	
	public Event(Type t, double endTime, Flow f) {
		this.t = t;
		this.endTime = endTime;
		this.f = f;
	}
	
	public Event(Type t, double endTime, Network n) {
		this.t = t;
		this.endTime = endTime;
		this.n = n;
	}
	
	// updates the network to complete this event
	// if a new event is caused, return that event
	// returns null otherwise
	public Event resolve(PriorityQueue<Event> q) {
		// might not need this switch anymore
		switch(t) {
		case BFORD:
//			StreamManager.print("routing", "Recalculating...\n");
			n.realBellmanFord();
			if(!q.isEmpty()) {
				Event e = new Event(t.BFORD, n.currTime + 8000, n);
				q.add(e);
			}
			break;
		case START:
			f.init(q);
			break;
		case OPPOR:
			d.opportunity(q);
			break;
		case TRANS:
			l.pop(q);
			break;
//		case DELAY:
//			break;
		case COMPL:
			break;
		case TIMEOUT:
			f.timeout(q);
			break;
		default:
			break;	
		}
		
		
		// Print a bunch of junk to the graph stream
//		System.out.println(this);
		
		return null;
	}
	
	@Override
	public int compareTo(Event e) {
		// TODO Auto-generated method stub
		return Double.compare(endTime, e.endTime);
	}
	
	@Override
	public String toString() {
		NumberFormat f = new DecimalFormat("#0.0000");
		//String output = f.format(endTime) + "	";
		//if(p.isAck) { output += "1"; }
		//else {output += "0"; }
		String output = "Time: " + f.format(endTime);
		switch(t) {
		case BFORD:
			output+= "\nType: " + "BFORD";
			break;
		case START:
			output+= "\nType: " + "START";
			break;
		case OPPOR:
			output+= "\nType: " + "OPPOR";
			break;
		case TRANS:
			output+= "\nType: " + "TRANS";
			break;
		case TIMEOUT:
			output+= "\nType: " + "TIMEOUT";
			break;
		}
		
		if(p != null) {
			output += "\nSource: " + p.source.addr;
			if(p.isAck) { output += "\n - is ACK"; }
		}
		return output;
	}
}
