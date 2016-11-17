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
		Network n = NetworkLoader.loadNetwork("basic_network.txt");
		NetworkViewer.launchViewer(n); 
		System.out.println(n);
	}
	
	public static void testDesignerViewer() {
		DesignerViewer.launchDesigner(); 
	}
	
	public static void testCase0() throws IOException {
		System.setOut(new PrintStream(new BufferedOutputStream(new FileOutputStream("output.txt"))));
		
		Network n = NetworkLoader.loadNetwork("basic_network.txt");
		n.init();
		
		for(int i = 0; i < 11000000; i++) {
			n.tick();
		}
	}
}
