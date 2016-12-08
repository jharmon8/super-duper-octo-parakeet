package networking;

import io.StreamManager;

import java.awt.Color;
import java.awt.Graphics;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;

import io.NetworkSaver;

public class Network {
	// metadata
	private int time = 0; // granularity of time
	
	// components
	private ArrayList<Device> devices;
	private ArrayList<Link> links;
	private ArrayList<Flow> flows;
	
	private PriorityQueue<Event> q;
	public static double currTime = 0;
	
	public static int debug1 = 0;
	public static int debug2 = 0;
	
	public Network(int time, ArrayList<Device> d, ArrayList<Link> l, ArrayList<Flow> f) {
		this.time = time;
		
		devices = d;
		links = l;
		flows = f;
		
		q = new PriorityQueue<Event>();
	}
	
/*	public Network() {
		time = -3;
		
		devices = new ArrayList<Device>();
		links = new ArrayList<Link>();
		flows = new ArrayList<Flow>();
		
		q = new PriorityQueue<Event>();
	}*/
	
	public void init() {
		// Setup some streams
		try {
			PrintStream r_stream = new PrintStream(new BufferedOutputStream(new FileOutputStream("output_routing.txt")));
			StreamManager.addStream("routing", r_stream);
			PrintStream p_stream = new PrintStream(new BufferedOutputStream(new FileOutputStream("output_packet.txt")));
			StreamManager.addStream("packet", p_stream);
			PrintStream w_stream = new PrintStream(new BufferedOutputStream(new FileOutputStream("output_window.txt")));
			StreamManager.addStream("window", w_stream);
			PrintStream t_stream = new PrintStream(new BufferedOutputStream(new FileOutputStream("output_threshold.txt")));
			StreamManager.addStream("threshold", t_stream);
			PrintStream b_stream = new PrintStream(new BufferedOutputStream(new FileOutputStream("output_buffer.txt")));
			StreamManager.addStream("buffer", b_stream);
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
//		logStatsEarly();
		q.remove(e);
		e.resolve(q);
		logStats();
/*		if(e.t == Event.Type.BFORD) {
			for(Device dev : devices) {
				dev.printRoutingInfo();
			}
		}*/
		return true;
	}
	
	private void logStatsEarly() {
		// TODO Auto-generated method stub
		NumberFormat fmt = new DecimalFormat("#0.0000");
		for(Flow f : flows) {
			StreamManager.print("window", fmt.format(currTime - .0001) + "\t" + f.source.addr + "\t" + f.window + "\t" + (f.windowThreshold < 0 ? 0 : f.windowThreshold) + "\n");
		}
		for(Link l : links) {
			StreamManager.print("buffer", fmt.format(currTime - .0001) + "\t" + l.devices[0].addr + ":" + l.devices[1].addr + "\t" + l.getBufferOccupancy() + "\n");
		}
	}

	private void logStats() {
		// TODO Auto-generated method stub
		NumberFormat fmt = new DecimalFormat("#0.0000");
		for(Flow f : flows) {
			StreamManager.print("window", fmt.format(currTime - .0001) + "\t" + f.source.addr + "\t" + f.window + "\t" + (f.windowThreshold < 0 ? 0 : f.windowThreshold) + "\n");
		}
		for(Link l : links) {
			StreamManager.print("buffer", fmt.format(currTime) + "\t" + l.devices[0].addr + ":" + l.devices[1].addr + "\t" + l.getBufferOccupancy() + "\n");
		}
	}

	public void draw(Graphics g, int w, int h) {
		g.setColor(Color.white);
		g.fillRect(0,0,2000,2000);
		
		for(Link l : links) {
			l.draw(g);
		}
		for(Device d : devices) {
			d.draw(g);
		}
		for(Flow f : flows) {
			f.draw(g);
		}
		
		g.setColor(Color.black);
		g.drawString(""+debug1, 10, 10);
		g.drawString(""+debug2, 10, 25);
		g.drawString(""+(int)currTime, 10, 40);
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
//			d.clearRoutingTable();
			if(d.isHost()) {
				d.realBroadcast(d.addr, 0, q);
			}
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
	
	public boolean saveNetwork(String filename) throws IOException {
		return NetworkSaver.saveNetwork(devices, links, flows, filename);
	}
}
