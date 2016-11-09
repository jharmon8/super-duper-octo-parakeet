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
	private int currTime = 0;
	
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
	
	public void tick() {
		Event e = q.peek();
		currTime = e.endTime;
		q.remove(e);
		e.resolve(q);
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
	
	public int execBellmanFord() {
		return 0;
	}
}
