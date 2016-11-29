package networking;

public class Packet {
	// for keeping track of half-duplex links
	public Device tempDest;
	
	// real header info
	public Device source;
	public Device dest;
	public Flow f;
	public int size;
	public int id;
	boolean isAck;
	boolean isRouting;
	String destAddr;
	int dist;	
	
	public Packet(int size, Device source, Device dest, Flow f, int id) {
		this.source = source;
		this.dest = dest;
		this.size = size;
		this.f = f;
		this.isAck = false;
		this.isRouting = false;
		this.id = id;
	}
	
	public Packet(int size, Device source, Device dest, Flow f, boolean ack, int id) {
		this.source = source;
		this.dest = dest;
		this.size = size;
		this.f = f;
		this.isAck = ack;
		this.isRouting = false;
		this.id = id;
	}
	
/*	public Packet(int size, Device source, Device dest, Flow f, int id, int fastWave) {
		this.source = source;
		this.dest = dest;
		this.size = size;
		this.f = f;
		this.isAck = false;
		this.isRouting = false;
		this.id = id;
	}
	
	public Packet(int size, Device source, Device dest, Flow f, boolean ack, int id) {
		this.source = source;
		this.dest = dest;
		this.size = size;
		this.f = f;
		this.isAck = ack;
		this.isRouting = false;
		this.id = id;
	}*/
	
	public Packet(int size, Device source, String destAddr, int distance, int id) {
		this.size = size;
		this.source = source;
		this.destAddr = destAddr;
		this.dist = distance;
		this.id = id;
		this.isAck = false;
		this.isRouting = true;
	}
	
	public Packet(int size, Device source, boolean ack, int id) {
		this.size = size;
		this.source = source;
		this.isAck = ack;
		this.isRouting = true;
		this.id = id;
	}
	
	@Override
	public String toString() {
		return "" + id + (isAck ? " ACK" : " DATA");
	}
}
