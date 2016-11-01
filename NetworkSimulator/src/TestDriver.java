import java.io.IOException;

import graphics.NetworkViewer;
import io.*;
import networking.*;


public class TestDriver {
	public static void main(String[] args) throws IOException {
		Network n = NetworkLoader.loadNetwork("pretty_network.txt");
		NetworkViewer.launchViewer(n); 
		System.out.println(n);
	}
}
