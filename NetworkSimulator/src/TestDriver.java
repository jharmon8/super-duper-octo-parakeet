import java.io.IOException;

import graphics.DesignerViewer;
import graphics.NetworkViewer;
import io.*;
import networking.*;


public class TestDriver {
	public static void main(String[] args) throws IOException {
		testCase0();
	}
	
	public static void testNetworkViewer() throws IOException {
		Network n = NetworkLoader.loadNetwork("Test_Case_1.txt");
		NetworkViewer.launchViewer(n); 
		System.out.println(n);
	}
	
	public static void testDesignerViewer() {
		DesignerViewer.launchDesigner(); 
	}
	
	public static void testCase0() throws IOException {
		Network n = NetworkLoader.loadNetwork("basic_network.txt");
		NetworkViewer.launchViewer(n); 
		System.out.println(n);
		n.init();
		
		for(int i = 0; i < 11000000; i++) {
			n.tick();
		}
	}
	public static void testCase1() throws IOException {
		Network n = NetworkLoader.loadNetwork("Test_Case_1.txt");
		NetworkViewer.launchViewer(n); 
		System.out.println(n);
		n.init();
		
		for(int i = 0; i < 11000000; i++) {
			n.tick();
		}
	}
}
