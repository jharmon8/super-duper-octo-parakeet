package io;

import java.io.PrintStream;
import java.util.ArrayList;

public class StreamManager {
	private static ArrayList<NamedStream> streams = new ArrayList<NamedStream>();
	
	public static void addStream(String name, PrintStream stream) {
		for(NamedStream ns : streams) {
			if(ns.name.equals(name)) {
				System.err.println("Stream " + name + " already exists");
				return;
			}
		}
		
		streams.add(new NamedStream(name, stream));
	}
	
	public static void print(String name, String message) {
		for(NamedStream ns : streams) {
			if(ns.name.equals(name)) {
				ns.print(message);
				return;
			}
		}
		
		System.err.println("Stream " + name + " doesn't exist");
	}
	
	private static class NamedStream {
		public String name;
		public PrintStream stream;
		
		public NamedStream(String name, PrintStream stream) {
			this.name = name;
			this.stream = stream;
		}
		
		public void print(String message) {
			stream.print(message);
			stream.flush();
		}
	}
}
