package networking;

import java.awt.Graphics;
import java.util.ArrayList;

public abstract class Device {
	public String addr;
	public int x, y;
	
	public Device(String addr) {
		this.addr = addr;
	}
	
	public abstract Packet request();
	public abstract void route(Packet p);
	public abstract boolean addLink(Link l);
	public abstract boolean setLinks(ArrayList<Link> links);
	public abstract ArrayList<Link> getLinks();
	public abstract void draw(Graphics g);
}
