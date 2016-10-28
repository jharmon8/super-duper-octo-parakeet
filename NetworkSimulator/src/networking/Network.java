package networking;

import java.awt.Graphics;
import java.util.ArrayList;

public class Network {
	// metadata
	private int time = 0; // granularity of time
	
	// components
	private ArrayList<Device> devices;
	private ArrayList<Link> links;
	private ArrayList<Flow> flows;
	
	public Network(int time, ArrayList<Device> d, ArrayList<Link> l, ArrayList<Flow> f) {
		this.time = time;
		
		devices = d;
		links = l;
		flows = f;
	}
	
	public void draw(Graphics g, int w, int h) {
		
	}
}
