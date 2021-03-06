package networking;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class RouteTable {
	private ArrayList<TableEntry> t = new ArrayList<TableEntry>();
	private ArrayList<Link> links = new ArrayList<Link>();
	private Router r;
	
	public RouteTable(Router r) {
		this.r = r;
		t.add(new TableEntry(r.addr, null, 0));
	}
	
	public Link getRoute(String addr) {
		for(TableEntry e : t) {
			if(e.addr.equals(addr)) {
				return e.l;
			}
		}
		
		return null;
	}
	
	public boolean addLink(Link l) {
		links.add(l);
		return true;
	}
	
	public ArrayList<Link> getLinks() {
		return links;
	}

	public void update(Link l, String dest, int dist, PriorityQueue<Event> q) {
		// find entry
		TableEntry entry = null;
		for(TableEntry e : t) {
			if(e.addr.equals(dest)) {
				entry = e;
				break;
			}
		}
		
		// entry doesn't exist
		if(entry == null) {
			t.add(new TableEntry(dest, l, dist + l.getMetric()));
			r.realBroadcast(dest, dist + l.getMetric(), q);
			return;
		}
		
		// entry needs updated
		if(entry.l == l) {
			entry.dist = dist + l.getMetric();
			r.realBroadcast(entry.addr, entry.dist, q);
			return;
		}
		
		// update is better than current entry
		if(dist + l.getMetric() < entry.dist) {
			entry.l = l;
			entry.dist = dist + l.getMetric();
			r.realBroadcast(entry.addr, entry.dist, q);
		}
	}
	
	private class TableEntry {
		public String addr;
		public Link l;
		public int dist;
		
		public TableEntry(String addr, Link l, int dist) {
			this.addr = addr;
			this.l = l;
			this.dist = dist;
		}
	}
	
	@Override
	public String toString() {
		String output = "";
		output+=r.addr + "\t" + Network.currTime + "\n";
		for(TableEntry e : t) {
			if(e.l == null) {
				output += "\t" + e.addr + ", " + e.dist + "\t" + r.addr + "\n";
			} else {
				output += "\t" + e.addr + ", " + e.dist + "\t" + e.l.otherDevice(r).addr + "\n";
			}
		}
		
		return output;
	}
}
