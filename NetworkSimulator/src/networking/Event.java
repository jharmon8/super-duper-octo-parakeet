package networking;

import java.util.PriorityQueue;

import networking.Event.Type;

public class Event implements Comparable<Event> {
	public enum Type {
		TRANS, /*DELAY,*/ COMPL
	}
	
	public Type t;
	public int endTime;
	public Flow f;
	public Link l;
	public Packet p;
	
	public Event(Type t, int endTime, /*Flow f, */Link l, Packet p) {
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
		return Integer.compare(endTime, e.endTime);
	}
	
	@Override
	public String toString() {
		return "Time: " + endTime + "\nSource: " + p.source.addr;
	}
}
