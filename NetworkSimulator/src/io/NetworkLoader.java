package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import networking.Network;

public class NetworkLoader {
	public Network loadNetwork(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String[] line = br.readLine().split(" ");
		int[] output = new int[line.length];
		
		for(int i = 0; i < line.length; i++) {
			output[i] = Integer.parseInt(line[i]);
		}
		
		br.close();
		// test
		return null;
	}
}
