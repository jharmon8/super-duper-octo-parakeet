package networking;

import java.awt.Graphics;
import java.util.ArrayList;

public class Network {
	private ArrayList<Device> devices;
	private ArrayList<Link> links;
	private ArrayList<Flow> flows;
	
	public Network(ArrayList<Device> d, ArrayList<Link> l, ArrayList<Flow> f) {
		devices = d;
		links = l;
		flows = f;
	}
	
	public void draw(Graphics g, int w, int h) {
		
	}
}
