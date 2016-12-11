import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import generator.NetworkGenerator;
import graphics.DesignerViewer;
import graphics.Launcher;
import graphics.NetworkViewer;
import io.NetworkLoader;
import networking.Network;


public class TestDriver {
	public static void main(String[] args) throws IOException {
		Launcher.launch();
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

	public static void testCase2() throws IOException {
		Network n = NetworkLoader.loadNetwork("Test_Case_2.txt");
		NetworkViewer.launchViewer(n); 
		System.out.println(n);
		n.init();
		
		while(n.tick()) {}
		
		System.out.println("Simulation finished.");
	}
	
	public static void reExport0() throws IOException {
		Network n = NetworkLoader.loadNetwork("basic_network.txt");
		System.out.println(n.saveNetwork("basic_export.txt"));
	}
	
	private static void viewCircle() {
		// TODO Auto-generated method stub
		Network n = NetworkGenerator.genCircularNetwork();
		NetworkViewer.launchViewer(n); 
	}
	
	private static void saveAndRunCircle() throws IOException {
		// TODO Auto-generated method stub
		Network n = NetworkGenerator.genCircularNetwork();
		
		n.saveNetwork("circle_test.txt");
		
		n = NetworkLoader.loadNetwork("circle_test.txt");
		NetworkViewer.launchViewer(n); 
		
		n.init();
		
		while(n.tick()) {}
		
		System.out.println("Simulation finished.");
	}
}
