package io;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

import networking.Device;
import networking.Flow;
import networking.Host;
import networking.Link;
import networking.Network;
import networking.Router;

public class NetworkSaver {
	/*
	 * Loads a network from a text file
	 */
	public static boolean saveNetwork(ArrayList<Device> devices,
										ArrayList<Link> links,
										ArrayList<Flow> flows,
										String filename) throws IOException {
		// Setup some streams
		try {
			PrintStream e_stream = new PrintStream(new BufferedOutputStream(new FileOutputStream(filename)));
			StreamManager.addStream("export", e_stream);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		// Leave a comment stating that this file was an export
		StreamManager.print("export", "# Network Export\n");
		
		// skip metadata
		StreamManager.print("export", "\n$\n\n");
		
		// devices
		for(Device d : devices) {
			StreamManager.print("export", d.exportString() + "\n");
		}
		
		StreamManager.print("export", "\n$\n\n");
		
		// links
		for(Link l : links) {
			StreamManager.print("export", l.exportString() + "\n");
		}
		
		StreamManager.print("export", "\n$\n\n");
		
		// flows
		for(Flow f : flows) {
			StreamManager.print("export", f.exportString() + "\n");
		}
		
		StreamManager.print("export", "\n$\n\n");
		
		return true;
	}
}
