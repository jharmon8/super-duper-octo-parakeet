package networking;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.PriorityQueue;

public class Event implements Comparable<Event> {
	public enum Type {
		OPPOR, TRANS, /*DELAY,*/ COMPL
	}
	
	public Type t;
	public double endTime;
	public Flow f;
	public Link l;
	public Packet p;
	public Device d;
	
	public Event(Type t, double endTime, Link l, Device d, Packet p) {
		// TODO Auto-generated constructor stub
		this.t = t;
		this.endTime = endTime;
//		this.f = f;
		this.l = l;
		this.d = d;
		this.p = p;
	}
	
	// updates the network to complete this event
	// if a new event is caused, return that event
	// returns null otherwise
	public Event resolve(PriorityQueue<Event> q) {
		// might not need this switch anymore
		switch(t) {
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
		default:
			break;	
		}
		
		
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
		//String output = f.format(endTime) + "	";
		//if(p.isAck) { output += "1"; }
		//else {output += "0"; }
		String output = "Time: " + f.format(endTime);
		switch(t) {
		case OPPOR:
			output+= "\nType: " + "OPPOR";
			break;
		case TRANS:
			output+= "\nType: " + "TRANS";
			break;
		}
		
		if(p != null) {
			output += "\nSource: " + p.source.addr;
			if(p.isAck) { output += "\n - is ACK"; }
		}
		return output;
	}
}
