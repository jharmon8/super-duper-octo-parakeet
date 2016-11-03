import java.io.IOException;

import graphics.DesignerViewer;
import graphics.NetworkViewer;
import io.*;
import networking.*;


public class TestDriver {
	public static void main(String[] args) throws IOException {
		testDesignerViewer();
	}
	
	public static void testNetworkViewer() throws IOException {
		Network n = NetworkLoader.loadNetwork("pretty_network.txt");
		NetworkViewer.launchViewer(n); 
		System.out.println(n);
	}
	
	public static void testDesignerViewer() {
		DesignerViewer.launchDesigner(); 
	}
}
