import java.io.IOException;

import graphics.DesignerViewer;
import graphics.NetworkViewer;
import io.*;
import networking.*;


public class TestDriver {
	public static void main(String[] args) throws IOException {
		testCaseNeg1();
	}
	
	public static void testNetworkViewer() throws IOException {
		Network n = NetworkLoader.loadNetwork("basic_network.txt");
		NetworkViewer.launchViewer(n); 
		System.out.println(n);
	}
	
	public static void testDesignerViewer() {
		DesignerViewer.launchDesigner(); 
	}
	
	public static void testCaseNeg1() throws IOException {
		Network n = NetworkLoader.loadNetwork("basic_network.txt");
		n.init();
		
		for(int i = 0; i < 11000000; i++) {
			n.tick();
		}
	}
}
