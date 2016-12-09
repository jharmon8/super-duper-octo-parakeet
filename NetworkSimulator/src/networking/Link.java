package networking;

import io.StreamManager;

import java.awt.Color;
import java.awt.Graphics;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.PriorityQueue;

import networking.Event.Type;

public class Link {
	//test
	String addr1, addr2;
	// rate in bps, latency in us, maxSize in KB
	int rate, latency, maxSize;
	Device[] devices = new Device[2];
	
	// the buffer and corresponding sources of each packet
	// (to keep track of directionality)
	LinkedList<Packet> buffer = new LinkedList<Packet>();
	LinkedList<Device> sources = new LinkedList<Device>();
	
	LinkedList<MetricPair> metrics = new LinkedList<MetricPair>();
	private double timeToConsider = 1000;
	
	// The end time that the last packet will finish transmitting (no delay)
	// Used when new packets are added to the buffer and need trans events
	private double bufferEndTime = 0;
	private int metric = 0;
	
	public Link(Device d1, Device d2, int rate, int latency, int maxSize) {
		this.devices[0] = d1;
		this.devices[1] = d2;
		this.rate = rate;
		this.latency = latency;
		this.maxSize = maxSize;
		this.metrics.add(new MetricPair(0,0));
	}
	
	public Link(Device d1, Device d2, String rate, String latency, String maxSize) {
		this.devices[0] = d1;
		this.devices[1] = d2;
		this.rate = Integer.parseInt(rate);
		this.latency = Integer.parseInt(latency);
		this.maxSize = Integer.parseInt(maxSize);
		this.metrics.add(new MetricPair(0,0));
	}
	
	public boolean containsDevice(Device d) {
		return (devices[0] == d || devices[1] == d);
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
	
	public void addPacket(Device source, Packet p, PriorityQueue<Event> q) {
		NumberFormat f = new DecimalFormat("#0.0000");
		
		Device dest = source == devices[0] ? devices[1] : devices[0];
		double temp = buffer.isEmpty() ? Network.currTime : bufferEndTime;
		
		int bufferSaturation = getBufferOccupancy();
		if(bufferSaturation + p.size > maxSize) { 
//				System.err.println("packet dropped");
//				NumberFormat f = new DecimalFormat("#0.0000");
//				StreamManager.print("packet", f.format(Network.currTime) + "\t" + p.id + "\t" + "DROP\n");
			f = new DecimalFormat("#0.0000");
			StreamManager.print("loss", f.format(Network.currTime) + "\t" + this.toString() + "\t" + 1 + "\t" + "\n");
			return; 
		}
		
		StreamManager.print("loss", f.format(Network.currTime) + "\t" + this.toString() + "\t" + 0 + "\t" + "\n");
		
		double transTime = ((double)p.size / rate) * 1000;
		if(!p.isRouting && !p.isAck) {
			metric += p.size;
//			System.out.println(metric);
		}
		
		StreamManager.print("link", f.format(temp) + "\t" + this.toString() + "\t" + 1 + "\t" + "\n");
		StreamManager.print("link", f.format(temp + transTime - .002) + "\t" + this.toString() + "\t" + 1 + "\t" + "\n");
		StreamManager.print("link", f.format(temp + transTime - .001) + "\t" + this.toString() + "\t" + 0 + "\t" + "\n");
		
		// if this source is the same as the last packet
		if(sources.size() == 0 || source != sources.get(sources.size() - 1)) {
			// gotta worry about delay
			temp += transTime + latency;
		} else {
			// don't worry about delay
			temp += transTime;
		}
		
		// create the events
/*		Event e1 = new Event(
				Type.OPPOR, 
				//TODO undo this 
				bufferEndTime + transTime, 
//				this, 
				this, 
				source,
				p
		);
		q.add(e1);*/
		Event e2 = new Event(
				Type.TRANS, 
				//TODO undo this 
				temp, 
//				this, 
				this, 
				source,
				p
		);
		q.add(e2);
		
		bufferEndTime = temp;
		
		// add the packet and source to buffer
		buffer.add(p);
		sources.add(source);
		
		updateMetrics(Network.currTime, getBufferOccupancy());
	}
	
	private void updateMetrics(double currTime, int metric) {
		// TODO Auto-generated method stub
		ArrayList<MetricPair> toRemove = new ArrayList<MetricPair>();

		for(MetricPair m : metrics) {
			if(m.time < currTime - timeToConsider) {
				toRemove.add(m);
			}
		}
		
		for(MetricPair m : toRemove) {
			metrics.remove(m);
		}
		
		metrics.add(new MetricPair(currTime, metric));
	}

	// Send the top packet on the buffer on its merry way
	public void pop(PriorityQueue<Event> q) {
		
		Packet pack = buffer.remove(0);
		Device src = sources.remove(0);
		Device dest = src == devices[0] ? devices[1] : devices[0];
		
		dest.route(this, pack, q);
		
		if(buffer.isEmpty()) {
			Event e1 = new Event(
					Type.OPPOR,
//					-1, // I think -1 will ensure the event happens next?
					Network.currTime,
					this,
					dest,
					pack
			);
			q.add(e1);
			Event e2 = new Event(
					Type.OPPOR,
//					-1, // I think -1 will ensure the event happens next?
					Network.currTime,
					this,
					src,
					pack
			);
			q.add(e2);
		}
		
		updateMetrics(Network.currTime, getBufferOccupancy());
	}
	
	// bypass all the shit
	// (this is why our bellman-ford is "cheating")
	public void send(Device src, String addr, int dist) {
		if(devices[0] == src) {
			devices[1].bfReceive(this, addr, dist);
		} else {
			devices[0].bfReceive(this, addr, dist);
		}
	}
	
	public int getMetric() {
//		int temp = metric;
//		metric = 0;
//		return metric + 1;
//		return (int) ((double) bufferSaturation / rate) * 1000 + 1;
		
		updateMetrics(Network.currTime, getBufferOccupancy());
		
/*		double integral = 0;
		double lastTime = Network.currTime - timeToConsider;
		
		for(MetricPair m : metrics) {
			if(lastTime > m.time) {
				System.err.println("Metric");
				System.exit(1);
			}
			
			integral+=m.m * (m.time - lastTime);
			lastTime = m.time;
		}
		
		return (int) (integral / timeToConsider + 1);*/
		return getBufferOccupancy() + 1;
		
//		return 1;
		
/*		int test = 0;
		for(Packet p : buffer) {
			if(!p.isRouting) {
				test++;
			}
		}
		
		return test + 1;*/
	}
	
	public Device otherDevice(Device devIn) {
		return devIn == devices[0] ? devices[1] : devices[0];
	}

	public int getBufferOccupancy() {
		// TODO Auto-generated method stub
		int bufferSaturation = 0;
		for(Packet packet : buffer) {
			bufferSaturation += packet.size;
		}
		return bufferSaturation;
	}
	
	@Override
	public String toString() {
		return devices[0].addr + ":" + devices[1].addr;
	}
	
	private class MetricPair {
		double time;
		int m;
		
		public MetricPair(double t, int m) {
			this.time = t;
			this.m = m;
		}
	}

	public String exportString() {
		// TODO Auto-generated method stub
		String output = devices[0].addr + " " + 
						devices[1].addr + " " + 
						rate + " " +
						latency + " " +
						maxSize;
		
		return output;
	}
}
