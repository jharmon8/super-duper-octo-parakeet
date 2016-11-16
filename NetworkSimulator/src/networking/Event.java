package networking;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.PriorityQueue;

public class Event implements Comparable<Event> {
	public enum Type {
		TRANS, /*DELAY,*/ COMPL
	}
	
	public Type t;
	public double endTime;
	public Flow f;
	public Link l;
	public Packet p;
	
	public Event(Type t, double endTime, /*Flow f, */Link l, Packet p) {
		// TODO Auto-generated constructor stub
		this.t = t;
		this.endTime = endTime;
//		this.f = f;
		this.l = l;
		this.p = p;
	}
	
	// updates the network to complete this event
	// if a new event is caused, return that event
	// returns null otherwise
	public Event resolve(PriorityQueue<Event> q) {
		// might not need this switch anymore
		switch(t) {
		case TRANS:
			break;
//		case DELAY:
//			break;
		case COMPL:
			break;
		default:
			break;	
		}
		
		l.pop(q);
		
		// Print a bunch of junk to the graph stream
		System.out.println(this);
		
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
		String output = "Time: " + f.format(endTime) + "\nSource: " + p.source.addr;
		if(p.isAck) { output += "\n - is ACK"; }
		return output;
	}
}
