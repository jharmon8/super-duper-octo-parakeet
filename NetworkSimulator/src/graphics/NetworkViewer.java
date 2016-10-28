package graphics;

import javax.swing.JFrame;

import networking.Network;

public class NetworkViewer {

	public static void launchViewer(Network n) {
		// TODO Auto-generated method stub
		JFrame f = new JFrame("Network Viewer v1.0");
		NetworkPanel p = new NetworkPanel(n);
		f.add(p);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
