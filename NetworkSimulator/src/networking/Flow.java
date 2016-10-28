package networking;

public class Flow {
	String addr1, addr2, packetSize, window;
	
	public Flow(String addr1, String addr2, String packetSize, String window) {
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.packetSize = packetSize;
		this.window = window;
	}
}
