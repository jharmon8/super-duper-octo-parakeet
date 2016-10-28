package networking;

import java.awt.Graphics;
import java.util.ArrayList;


public class Host extends Device {
	private Link link; // this might be removed
	private String hostname;
	private int x, y;
	
	public Host(String addr, String x, String y, String hostname) {
		// TODO Auto-generated constructor stub
		super(addr);
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
		this.hostname = hostname;
	}

	public Host(String addr, String x, String y) {
		// TODO Auto-generated constructor stub
		super(addr);
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
		hostname = "<no name>";
	}

	@Override
	public Packet request() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void route(Packet p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean addLink(Link l) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setLinks(ArrayList<Link> links) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Link> getLinks() {
		// TODO Auto-generated method stub
		ArrayList<Link> output = new ArrayList<Link>();
		output.add(link);
		
		return output;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}

}
