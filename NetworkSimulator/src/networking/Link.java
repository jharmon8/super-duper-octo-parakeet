package networking;

import java.awt.Color;
import java.awt.Graphics;

public class Link {
	//test
	String addr1, addr2;
	int rate, latency;
	Device[] devices = new Device[2];
	
	public Link(Device d1, Device d2, int rate, int latency) {
		this.devices[0] = d1;
		this.devices[1] = d2;
		this.rate = rate;
		this.latency = latency;
	}
	
	public Link(Device d1, Device d2, String rate, String latency) {
		this.devices[0] = d1;
		this.devices[1] = d2;
		this.rate = Integer.parseInt(rate);
		this.latency = Integer.parseInt(latency);
	}

	public void draw(Graphics g) {
		// TODO Auto-generated method stub
		g.setColor(Color.BLACK);
		g.drawLine(devices[0].x, devices[0].y, devices[1].x, devices[1].y);
		
		int midX = (devices[0].x + devices[1].x) / 2;
		int midY = (devices[0].y + devices[1].y) / 2;
		
		g.fillOval(midX - 2, midY - 2, 4, 4);
		
		int leftY = devices[0].x < devices[1].x ? devices[0].y : devices[1].y;
		int rightY = devices[0].x < devices[1].x ? devices[1].y : devices[0].y;
		
		if(leftY > rightY + 5) {
			g.drawString("Rate: " + rate, midX + 10, midY + 10);
			g.drawString("Latency: " + latency, midX + 10, midY + 20);
		} else {
			g.drawString("Rate: " + rate, midX + 10, midY - 25);
			g.drawString("Latency: " + latency, midX + 10, midY - 15);
		}
	}
}
