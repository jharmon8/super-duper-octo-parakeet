package networking;

public class Event implements Comparable<Event> {
	
	public int endTime;
	
	public Event(int time) {
		// TODO Auto-generated constructor stub
		endTime = time;
	}
	
	@Override
	public int compareTo(Event e) {
		// TODO Auto-generated method stub
		return Integer.compare(endTime, e.endTime);
	}
}
