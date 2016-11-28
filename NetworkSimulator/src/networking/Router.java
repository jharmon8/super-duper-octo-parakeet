package networking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.PriorityQueue;

import io.StreamManager;

public class Router extends Device {
	RouteTable rt = new RouteTable(this);
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
	public void request(Packet p, PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		return;
	}

	@Override
	public void route(Link l, Packet p, PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		if(p.isRouting) {
			if(p.isAck) {
				// TODO do something
				return;
			}
			
			rt.update(l, p.destAddr, p.dist, q);
			Packet ack = new Packet(64, this, true, p.id);
			l.addPacket(this, ack, q);
			return;
		}
		
		Link route = rt.getRoute(p.dest.addr);
		if(route == null) {
			System.err.println("Dropping packet due to missing routing entry");
			return;
		}
		
		if(!p.isAck) {
			StreamManager.print("routing", Network.currTime + "\t" + this.addr + "\n");
		}
		
		route.addPacket(this, p, q);
	}

	@Override
	public boolean addLink(Link l) {
		// TODO Auto-generated method stub
		return rt.addLink(l);
	}

	@Override
	public boolean setLinks(ArrayList<Link> links) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ArrayList<Link> getLinks() {
		// TODO Auto-generated method stub
		return rt.getLinks();
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

	@Override
	// I don't think routers need this?
	public Link getSingleLink() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void broadcast(Device dest, int dist) {
		// TODO Auto-generated method stub
		ArrayList<Link> links = rt.getLinks();
		for(Link l : links) {
			l.send(this, dest.addr, dist);
		}
	}
	
	public void realBroadcast(String dest, int dist, PriorityQueue<Event> q) {
		ArrayList<Link> links = rt.getLinks();
		for(Link l : links) {
			Packet p = new Packet(64, this, dest, dist, 1);
			l.addPacket(this, p, q);
		}
	}

	@Override
	public void bfReceive(Link link, String dest, int dist) {
		// TODO Auto-generated method stub
//		rt.update(link, dest, dist, q);
	}

	@Override
	public void opportunity(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		// don't do anything?
	}

	@Override
	public void addFlow(Flow f) {
		// TODO Auto-generated method stub
		// This might be useful when we TCP routing packets
	}

	@Override
	public void printRoutingInfo() {
		// TODO Auto-generated method stub
		System.out.println(rt);
	}

	@Override
	public void clearRoutingTable() {
		// TODO Auto-generated method stub
		rt = new RouteTable(this);
	}
}
