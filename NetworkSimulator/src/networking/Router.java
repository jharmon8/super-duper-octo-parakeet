package networking;

import java.awt.Graphics;
import java.util.ArrayList;


public class Router extends Device {
	ArrayList<Link> links = new ArrayList<Link>();
	
	public Router(String addr) {
		// TODO Auto-generated constructor stub
		super(addr);
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
		return links;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		
	}
}
