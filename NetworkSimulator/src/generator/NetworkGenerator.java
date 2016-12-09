package generator;

import java.util.ArrayList;
import java.util.Random;

import networking.*;

public class NetworkGenerator {
	private static int S_WIDTH = 1024;
	private static int S_HEIGHT = 1024;
	
	private static final int NUM_ROUTERS = 16;
	private static final int NUM_HOSTS = 32;
	private static final int MAX_LINKS = 3;
	private static final int NUM_FLOWS = 12;
	
	private static final int LINK_RATE = 1000000;
	private static final int LINK_LATENCY = 10;
	private static final int LINK_BUFFER = 64000;
	
	private static final String FLOW_PSIZE = "1024";
	private static final String FLOW_CONGT = "1";
	private static final String FLOW_D_AMT = "20000000";
	private static final String FLOW_DELAY = "10000";
	
	private static final double MAIN_RADIUS = 400;
	private static final double LINK_RADIUS = 250;
	private static final double SPACE_RADIUS = 125;
	
//	Network n = new Network();
	
	private static Random rand = new Random(1234);
	
	public static Network genCircularNetwork() {
		ArrayList<Host> hosts = new ArrayList<Host>();
		ArrayList<Router> routers = new ArrayList<Router>();
		ArrayList<Link> links = new ArrayList<Link>();
		ArrayList<Flow> flows = new ArrayList<Flow>();
		
		
		// init hosts
		double theta = 0;
		double dist = 500;
		
		for(int i = 0; i < NUM_HOSTS; i++) {
			int x = (int) (Math.sin(theta) * dist) + S_WIDTH / 2;
			int y = (int) (Math.cos(theta) * dist) + S_HEIGHT / 2;
			
			hosts.add(new Host(hosts.size()+"", x+"", y+""));
			
			theta += 2 * Math.PI / NUM_HOSTS;
		}
		
		// gen routers
		while(routers.size() < NUM_ROUTERS) {
			int x = rand.nextInt(S_WIDTH);
			int y = rand.nextInt(S_HEIGHT);
			
			if(distance(x, y, S_WIDTH/2, S_HEIGHT/2) < MAIN_RADIUS) {
				boolean wellSpaced = true;
				for(Router r : routers) {
					if(distance(x, y, r.x, r.y) < SPACE_RADIUS) {
						wellSpaced = false;
						break;
					}
				}
				
				if(wellSpaced) {
					routers.add(new Router(routers.size()+NUM_HOSTS+"", x+"", y+""));
				}
			}
		}
		
		// link routers
		for(int i = 0; i < routers.size(); i++) {
			for(int j = i; j < routers.size(); j++) {
				Router r = routers.get(i);
				Router t = routers.get(j);
				
				if(distance(r.x, r.y, t.x, t.y) < LINK_RADIUS) {
//					if(links.size() < MAX_LINKS) {
					links.add(new Link(r, t, LINK_RATE, LINK_LATENCY, LINK_BUFFER));
//					}
				}
			}
		}
		
		// link hosts
		for(Host h : hosts) {
			double minDist = -1;
			Router closest = null;
			
			for(Router r : routers) {
				if(distance(h.x, h.y, r.x, r.y) < minDist || minDist < 0) {
					closest = r;
					minDist = distance(h.x, h.y, r.x, r.y);
				}
			}
			
			if(closest != null) {
				links.add(new Link(h, closest, LINK_RATE, LINK_LATENCY, LINK_BUFFER));
			}
		}
		
		// Create flows
		while(flows.size() < NUM_FLOWS) {
			int first = rand.nextInt(hosts.size());
			int second = rand.nextInt(hosts.size());
			
			if(first == second) { continue; }
			if(first == 0 || first == NUM_HOSTS/2) { continue; }
			if(second == 0 || second == NUM_HOSTS/2) { continue; }
			
			Host src = hosts.get(first);
			Host dest = hosts.get(second);
			
			boolean valid = true;
			for(Flow f : flows) {
//				if(f.entails(src) || f.entails(dest)) {
//					valid = false;
//				}
			}
			if(!valid) { continue; }
			
//			flows.add(new Flow(src, dest, FLOW_PSIZE, FLOW_CONGT, FLOW_D_AMT, FLOW_DELAY));
		}
		
		// Export
		ArrayList<Device> devices = new ArrayList<Device>();
		for(Host h : hosts) {
			devices.add(h);
		}
		
		for(Router r : routers) {
			devices.add(r);
		}
		
		return new Network(devices, links, flows);
	}
	
	private static double distance(int x1, int y1, int x2, int y2) {
		int dx = x1 - x2;
		int dy = y1 - y2;
		
		return Math.sqrt(dx * dx + dy * dy);
	}
}
