package networking;

import java.awt.Graphics;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.PriorityQueue;

import io.StreamManager;

public class Network {
	// metadata
	private int time = 0; // granularity of time
	
	// components
	private ArrayList<Device> devices;
	private ArrayList<Link> links;
	private ArrayList<Flow> flows;
	
	private PriorityQueue<Event> q;
	public static double currTime = 0;
	
	public Network(int time, ArrayList<Device> d, ArrayList<Link> l, ArrayList<Flow> f) {
		this.time = time;
		
		devices = d;
		links = l;
		flows = f;
		
		q = new PriorityQueue<Event>();
	}
	
	public void init() {
		// Setup some streams
		try {
			PrintStream r_stream = new PrintStream(new BufferedOutputStream(new FileOutputStream("output_routing.txt")));
			StreamManager.addStream("routing", r_stream);
			PrintStream p_stream = new PrintStream(new BufferedOutputStream(new FileOutputStream("output_packet.txt")));
			StreamManager.addStream("packet", p_stream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		
		// prepare the flows
		for(Flow f : flows) {
//			f.init(q);
			f.addStartEvent(q);
		}
		
		// start building the routing tables
//		execBellmanFord();
//		realBellmanFord();
		q.add(new Event(Event.Type.BFORD, 0, this));
	}
	
	// returns true if anything happens
	public boolean tick() {
		if(!releveantQ()) {return false;}
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
	
	public void realBellmanFord() {
		for(Device d : devices) {
			d.realBroadcast(d.addr, 0, q);
		}
	}
	
	public boolean releveantQ() {
		for(Event ev : q) {
			if(ev.t == Event.Type.TRANS) {
				if(!ev.p.isRouting) {
					return true;
				}
			}
			if(ev.t == Event.Type.START) {
				return true;
			}
			if(ev.t == Event.Type.OPPOR) {
				return true;
			}
			if(ev.t == Event.Type.TIMEOUT) {
				return true;
			}
		}
		return false;
	}
}
