package graphics;

import javax.swing.JFrame;

import networking.Network;

public class DesignerViewer {

	public static void launchDesigner() {
		// TODO Auto-generated method stub
		JFrame f = new JFrame("Network Viewer v1.0");
		DesignerPanel p = new DesignerPanel();
		f.add(p);
		f.pack();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}
