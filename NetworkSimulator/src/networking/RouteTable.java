package networking;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class RouteTable {
	private ArrayList<TableEntry> t = new ArrayList<TableEntry>();
	private ArrayList<Link> links = new ArrayList<Link>();
	private Router r;
	
	public RouteTable(Router r) {
		this.r = r;
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
		// TODO Auto-generated method stub
		int oldDist = -1;
		
		for(TableEntry e : t) {
			if(e.addr.equals(dest)) {
				oldDist = e.dist;
				break;
			}
		}
		
		if(oldDist == -1) {
			t.add(new TableEntry(dest, l, dist + 1));
			r.realBroadcast(dest, dist + 1, q);
			return;
		}
		
		if(oldDist > dist) {
			for(TableEntry e : t) {
				if(e.addr.equals(dest)) {
					e.l = l;
					e.dist = dist;
					t.add(new TableEntry(dest, l, dist + 1));
					r.realBroadcast(dest, dist + 1, q);
					break;
				}
			}
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
}
