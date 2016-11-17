package networking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.PriorityQueue;


public class Host extends Device {
	private Link link; // this might be removed
	private String hostname;
	
	private final int ACK_SIZE = 64;
	
	public Host(String addr, String x, String y, String hostname) {
		// TODO Auto-generated constructor stub
		super(addr);
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
		this.hostname = hostname;
	}

	public Host(String addr, int x, int y, String hostname) {
		// TODO Auto-generated constructor stub
		super(addr);
		this.x = x;
		this.y = y;
		this.hostname = hostname;
	}

	public Host(String addr, String x, String y) {
		// TODO Auto-generated constructor stub
		super(addr);
		this.x = Integer.parseInt(x);
		this.y = Integer.parseInt(y);
		hostname = "<no name>";
	}

	public Host(String addr, int x, int y) {
		// TODO Auto-generated constructor stub
		super(addr);
		this.x = x;
		this.y = y;
		hostname = "<no name>";
	}

	@Override
	public void request(Packet p, PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		link.addPacket(this, p, q);
	}

	// this is really simple for hosts...
	// nvm, this is less simple with congestion
	@Override
	public void route(Packet p, PriorityQueue<Event> q) {
		if(!p.isAck) {
			Packet ack = new Packet(ACK_SIZE, this, p.f, true);
			// on your merry way, now!
			link.addPacket(this, ack, q);
		} else {
			p.f.acknowledge(p, q);
		}
	}

	@Override
	public boolean addLink(Link l) {
		// TODO Auto-generated method stub
		if(link != null) {return false;}
		this.link = l;
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
		ArrayList<Link> output = new ArrayList<Link>();
		output.add(link);
		
		return output;
	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		int size = 10;
		g.setColor(Color.BLUE);
		g.fillOval(x - size/2, y - size/2, size, size);
		g.drawString(addr, x, y + 2 * size);
		g.drawString(hostname, x, y + 2 * size + 10);
	}

	@Override
	public boolean isHost() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Link getSingleLink() {
		// TODO Auto-generated method stub
		return link;
	}

	@Override
	public void broadcast(Device dest, int dist) {
		// TODO Auto-generated method stub
		// don't do anything
	}

}
