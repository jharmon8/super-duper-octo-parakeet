import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import graphics.DesignerViewer;
import graphics.NetworkViewer;
import io.NetworkLoader;
import networking.Network;


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
		//System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
		
		Network n = NetworkLoader.loadNetwork("basic_network.txt");
		NetworkViewer.launchViewer(n); 
		System.out.println(n);
		n.init();
		
		while(n.tick()) {}

		System.out.println("Simulation finished.");
}
	
	public static void testCase1() throws IOException {
		Network n = NetworkLoader.loadNetwork("Test_Case_1.txt");
		NetworkViewer.launchViewer(n); 
		System.out.println(n);
		n.init();
		
		while(n.tick()) {}
		
		System.out.println("Simulation finished.");
	}
}
