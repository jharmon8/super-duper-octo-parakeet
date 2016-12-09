package networking;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.PriorityQueue;

import io.StreamManager;


public class Host extends Device {
	private Link link; // this might be removed
	private String hostname;
	
	private ArrayList<Packet> received = new ArrayList<Packet>();
	private int lastAck = 1;
	private final int ACK_SIZE = 64;
	private boolean needSendAck = false;
	
	// I think this is neccessary
	private ArrayList<Flow> flows = new ArrayList<Flow>();
	
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
//		NumberFormat f = new DecimalFormat("#0.0000");
//		StreamManager.print("packet", f.format(Network.currTime) + "\t" + addr + "\t" + p.id + "\n");
	}

	// this is really simple for hosts...
	// nvm, this is less simple with congestion
	@Override
	public void route(Link l, Packet p, PriorityQueue<Event> q) {
		if(p.isRouting) {return;}
		
		if(!p.isAck) {
			received.add(p);
			needSendAck = true;
//			Packet ack = new Packet(ACK_SIZE, this, p.source, p.f, true, getAckId());
			// on your merry way, now!
//			link.addPacket(this, ack, q);
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

	@Override
	public void bfReceive(Link link, String dest, int dist) {
		// TODO Auto-generated method stub
		// also don't do anything
	}

	@Override
	public void opportunity(PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		// ask the flow what to do
//		for(Flow f : flows) {
//			if(f.opportunity(this, q)) {
//				break;
//			}
//		}
		if(needSendAck) {
			sendAcknowledgement(q);
//			lastAck = getAckId();
			needSendAck = false;
		}
		
		return;
	}

	@Override
	public void addFlow(Flow f) {
		// TODO Auto-generated method stub
		flows.add(f);
	}
	
	// send an ack packet
	private boolean sendAcknowledgement(PriorityQueue<Event> q) {
		// on your merry way, now!
		Packet p = received.get(0);
		Packet ack = new Packet(ACK_SIZE, this, p.source, p.f, true, getAckId());
		ack.batch = p.batch;
		link.addPacket(this, ack, q);
//		NumberFormat f = new DecimalFormat("#0.0000");
//		StreamManager.print("packet", f.format(Network.currTime) + "\t" + addr + "\t" + ack.id + "\n");
		return false;
	}
	
	private int getAckId() {
		if(received.isEmpty()) {return -1;}
		ArrayList<Packet> toRemove = new ArrayList<Packet>();

		int i = lastAck;
		
		while(true) {
			boolean found = false;
			for(Packet p : received) {
				if(p.id == i) {
					i++;
					found = true;
					toRemove.add(p);
					break;
				}
			}
			
			if(!found) {break;}
		}
		
		if(!toRemove.isEmpty()) {
			toRemove.remove(toRemove.size() - 1);
			cleanReceived(toRemove);
		}
		
		lastAck = i;
		return i;
	}
	
	private void cleanReceived(ArrayList<Packet> toRemove) {
		
	}

	@Override
	public void realBroadcast(String dest, int dist, PriorityQueue<Event> q) {
		// TODO Auto-generated method stub
		Packet p = new Packet(64, this, dest, dist, 1);
		link.addPacket(this, p, q);
	}

	@Override
	public void printRoutingInfo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearRoutingTable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String exportString() {
		// TODO Auto-generated method stub
		String output = "1 " + addr + " " + x + " " + y;
		if(!hostname.equals("<no name>")) {
			output += " " + hostname;
		}
		
		return output;
	}
}
