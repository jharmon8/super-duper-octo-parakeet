package networking;

public class Packet {
	// for keeping track of half-duplex links
	public Device tempDest;
	
	// real header info
	public Device source;
	public Device dest;
	public Flow f;
	public int size;
	boolean isAck;
	
	public Packet(int size, Device source, Flow f) {
		this.source = source;
		this.size = size;
		this.f = f;
		this.isAck = false;
	}
	
	public Packet(int size, Device source, Flow f, boolean ack) {
		this.source = source;
		this.size = size;
		this.f = f;
		this.isAck = ack;
	}
}
