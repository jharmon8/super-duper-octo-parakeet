package networking;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Network {
	// metadata
	private int time = 0; // granularity of time
	
	// components
	private ArrayList<Device> devices;
	private ArrayList<Link> links;
	private ArrayList<Flow> flows;
	
	private PriorityQueue<Event> q;
	private double currTime = 0;
	
	public Network(int time, ArrayList<Device> d, ArrayList<Link> l, ArrayList<Flow> f) {
		this.time = time;
		
		devices = d;
		links = l;
		flows = f;
		
		q = new PriorityQueue<Event>();
	}
	
	public void init() {
		execBellmanFord();
		
		for(Flow f : flows) {
			f.init(q);
		}
	}
	
	// returns true if anything happens
	public boolean tick() {
		if(q.isEmpty()) {return false;}
		Event e = q.peek();
		currTime = e.endTime;
		q.remove(e);
		e.resolve(q);
		return true;
	}
	
	public void draw(Graphics g, int w, int h) {
		for(Link l : links) {
			l.draw(g);
		}
		for(Device d : devices) {
			d.draw(g);
		}
		for(Flow f : flows) {
			f.draw(g);
		}
	}

	// Setup the routing tables
	public int execBellmanFord() {
		for(Device d : devices) {
			d.broadcast(d, 0);
		}
		
		return -1;
	}
}
