package io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import networking.Network;

public class NetworkLoader {
	public Network loadNetwork(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = "";
		
		while((line = br.readLine()) != null) {
			
		}
		
		br.close();
		return null;
	}
}
