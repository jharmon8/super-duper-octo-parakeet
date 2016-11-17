package networking;

import java.util.ArrayList;

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

	public void update(Link l, String dest, int dist) {
		// TODO Auto-generated method stub
		int oldDist = -1;
		
		for(TableEntry e : t) {
			if(e.addr.equals(dest)) {
				oldDist = e.dist;
			}
		}
		
		if(oldDist == -1) {
			t.add(new TableEntry(dest, l, dist));
			
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
