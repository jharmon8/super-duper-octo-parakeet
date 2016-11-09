package networking;

public class Packet {
	// for keeping track of half-duplex links
	public Device tempDest;
	
	// real header info
	public Device source;
	public Device dest;
	public int size;
}
