package networking;

public class Link {
	//test
	String addr1, addr2, rate, latency;
	
	public Link(String addr1, String addr2, String rate, String latency) {
		this.addr1 = addr1;
		this.addr2 = addr2;
		this.rate = rate;
		this.latency = latency;
	}
}
