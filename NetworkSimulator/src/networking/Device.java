package networking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.PriorityQueue;

public abstract class Device {
	public String addr;
	public int x, y;
	
	public Device(String addr) {
		this.addr = addr;
	}
	
	public abstract void request(Packet p, PriorityQueue<Event> q);
	public abstract void route(Packet p, PriorityQueue<Event> q);
	public abstract boolean addLink(Link l);
	public abstract boolean setLinks(ArrayList<Link> links);
	public abstract ArrayList<Link> getLinks();
	public abstract Link getSingleLink();
	public abstract void draw(Graphics g);
	public abstract boolean isHost();

	public void drawSelection(Graphics g) {
		int size = 20;
		g.setColor(Color.BLACK);
		g.fillOval(x-size/2, y-size/2, size, size);
	}
	
	public double getDistance(int x2, int y2) {
		int dx = x2 - x;
		int dy = y2 - y;
		
		return Math.sqrt(dx * dx + dy * dy);
	}
}
