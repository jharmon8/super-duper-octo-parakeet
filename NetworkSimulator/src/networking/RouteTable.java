package networking;

import java.util.HashMap;

public class RouteTable {
	private HashMap<String, Integer> t = new HashMap<String, Integer>();
	
	public RouteTable() {
		
	}
	
	public int getRoute(String addr) {
		return t.get(addr).intValue();
	}
}
