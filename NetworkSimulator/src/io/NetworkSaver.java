package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import networking.Device;
import networking.Flow;
import networking.Host;
import networking.Link;
import networking.Network;
import networking.Router;

public class NetworkSaver {
	/*
	 * Loads a network from a text file
	 */
	public static Network saveNetwork(String filename) throws IOException {
		// network variables
		int time = -6;
		ArrayList<Device> d = new ArrayList<Device>();
		ArrayList<Link> l = new ArrayList<Link>();
		ArrayList<Flow> f = new ArrayList<Flow>();
		
		// start reading
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = "";
		int section = 0;
		
		while((line = br.readLine()) != null) {
			if(line.length() == 0) {
				continue;
			}
			
			if(line.charAt(0) == '#') {
				continue;
			}
			
			if(line.contains("$")) {
				section++;
				continue;
			}
			
			String[] split;
			switch(section) {
			// metadata
			case 0:
				switch(line.charAt(0)) {
				case 't':
					split = line.split(" ");
					time = Integer.parseInt(split[1]);
					break;
				default:
					System.err.println("unknown metatdata tag");
					System.exit(1);
				}
				break;
			// devices
			case 1:
				split = line.split(" ");
				if(split[0].equals("1")) {
					Device dev = new Host(split[1], split[2], split[3], split[4]);
					d.add(dev);
				} else if(split[0].equals("2")) {
					Device dev = new Router(split[1], split[2], split[3], split[4]);
					d.add(dev);
				} else {
					System.err.println("unknown device type");
					System.exit(1);
				}
				break;
			// links
			case 2:
				split = line.split(" ");
				Device d1 = null, d2 = null;
				for(Device dev : d) {
					if(split[0].equals(dev.addr)) {
						d1 = dev;
					}
					if(split[1].equals(dev.addr)) {
						d2 = dev;
					}
				}
				
				Link link = new Link(d1, d2, split[2], split[3]);
				d1.addLink(link);
				d2.addLink(link);
				l.add(link);
				break;
			// flows
			case 3:
				split = line.split(" ");
				Flow flow = new Flow(split[0], split[1], split[2], split[3]);
				f.add(flow);
				break;
			default:
				System.err.println("too many sections in loader");
				System.exit(1);
			}
		}
		
		br.close();
		return new Network(time, d, l, f);
	}
}