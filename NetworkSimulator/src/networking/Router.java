package networking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;


public class Router extends Device {
	ArrayList<Link> links = new ArrayList<Link>();
	private int congestion;
	
	public Router(String addr, String x, String y, String cong) {
		// TODO Auto-generated constructor stub
		super(addr);
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
		this.congestion = Integer.parseInt(cong);
	}
	
	public Router(String addr, int x, int y, int cong) {
		// TODO Auto-generated constructor stub
		super(addr);
		this.x = x;
		this.y = y;
		this.congestion = cong;
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
		links.add(l);
		return true;
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
		int size = 10;
		g.setColor(Color.RED);
		g.fillOval(x - size/2, y - size/2, size, size);
		g.drawChars(addr.toCharArray(), 0, addr.length(), x, y + 2 * size);
	}

	@Override
	public boolean isHost() {
		// TODO Auto-generated method stub
		return false;
	}
}
