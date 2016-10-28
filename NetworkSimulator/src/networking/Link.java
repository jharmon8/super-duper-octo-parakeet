package networking;

import java.awt.Color;
import java.awt.Graphics;

public class Link {
	//test
	String addr1, addr2, rate, latency;
	Device[] devices = new Device[2];
	
	public Link(Device d1, Device d2, String rate, String latency) {
		this.devices[0] = d1;
		this.devices[1] = d2;
		this.rate = rate;
		this.latency = latency;
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLACK);
		g.drawLine(devices[0].x, devices[0].y, devices[1].x, devices[1].y);
	}
}
